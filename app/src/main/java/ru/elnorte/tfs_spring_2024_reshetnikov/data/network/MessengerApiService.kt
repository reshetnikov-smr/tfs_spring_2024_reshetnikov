package ru.elnorte.tfs_spring_2024_reshetnikov.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.GenericResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MessageSendingResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MessagesResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MineUserResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.PeopleResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.StreamsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.SubscribedStreamsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.TopicsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.UserPresenceResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.UsersPresenceResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.BASE_URL


const val API_KEY = "Z5cfmzT3hpgeHxjIr4DSbgXYMDyAY8kG"  //tfschat
const val AUTH_MAIL = "elnortemobile@gmail.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val authInterceptor = Interceptor { chain ->
    val credentials = Credentials.basic(AUTH_MAIL, API_KEY)
    val newRequest = chain.request()
        .newBuilder()
        .addHeader("Authorization", credentials)
        .build()
    chain.proceed(newRequest)

}


private val internetClient = OkHttpClient().newBuilder()
    .addInterceptor(authInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .client(internetClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

private val retrofitScalar = Retrofit.Builder()
    .client(internetClient)
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface MessengerApiService {
    @GET("users")
    suspend fun getUsers(): PeopleResponse

    @GET("users/{userId}/presence")
    suspend fun getUserPresence(@Path("userId") userId: Int): UserPresenceResponse

    @GET("realm/presence")
    suspend fun getUsersPresence(): UsersPresenceResponse

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): SubscribedStreamsResponse

    @GET("streams")
    suspend fun getStreams(): StreamsResponse

    @GET("users/me/{streamId}/topics")
    suspend fun getTopics(@Path("streamId") streamId: Int): TopicsResponse

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: String? = null,
    ): Call<MessagesResponse>

    @FormUrlEncoded
    @POST("messages")
    fun sendStreamMessage(
        @Field("type") type: String = "stream",
        @Field("to") to: String,
        @Field("topic") topic: String,
        @Field("content") content: String
    ): Call<MessageSendingResponse>

    @FormUrlEncoded
    @POST("messages")
    fun sendDirectMessage(
        @Field("type") type: String,
        @Field("to") to: String,
        @Field("content") content: String
    ): Call<MessageSendingResponse>

    @FormUrlEncoded
    @POST("messages/{messageId}/reactions")
    fun addEmoji(
        @Path("messageId") messageId: Int,
        @Field("emoji_name") emojiName: String
    ): Call<GenericResponse>

    @DELETE("messages/{messageId}/reactions")
    suspend fun removeEmoji(
        @Path("messageId") messageId: Int,
        @Query("emoji_name") emojiName: String
    ): GenericResponse

    @GET("users/me")
    suspend fun getMineUser(): MineUserResponse

}

object MessengerApi {
    val retrofitService: MessengerApiService by lazy { retrofit.create(MessengerApiService::class.java) }
}
