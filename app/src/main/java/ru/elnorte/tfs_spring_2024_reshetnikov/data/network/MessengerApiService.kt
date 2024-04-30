package ru.elnorte.tfs_spring_2024_reshetnikov.data.network

import retrofit2.Call
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


const val API_KEY = "Z5cfmzT3hpgeHxjIr4DSbgXYMDyAY8kG"
const val AUTH_MAIL = "elnortemobile@gmail.com"

interface MessengerApiService {
    @GET("users")
    fun getUsers(): Call<PeopleResponse>

    @GET("users/{userId}/presence")
    fun getUserPresence(@Path("userId") userId: Int): UserPresenceResponse

    @GET("realm/presence")
    fun getUsersPresence(): Call<UsersPresenceResponse>

    @GET("users/me/subscriptions")
    fun getSubscribedStreams(): Call<SubscribedStreamsResponse>

    @GET("streams")
    fun getStreams(): Call<StreamsResponse>

    @GET("users/me/{streamId}/topics")
    fun getTopics(@Path("streamId") streamId: Int): Call<TopicsResponse>

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
        @Field("content") content: String,
    ): Call<MessageSendingResponse>

    @FormUrlEncoded
    @POST("messages")
    fun sendDirectMessage(
        @Field("type") type: String,
        @Field("to") to: String,
        @Field("content") content: String,
    ): Call<MessageSendingResponse>

    @FormUrlEncoded
    @POST("messages/{messageId}/reactions")
    fun addEmoji(
        @Path("messageId") messageId: Int,
        @Field("emoji_name") emojiName: String,
    ): Call<GenericResponse>

    @DELETE("messages/{messageId}/reactions")
    fun removeEmoji(
        @Path("messageId") messageId: Int,
        @Query("emoji_name") emojiName: String,
    ): Call<GenericResponse>

    @GET("users/me")
    fun getMineUser(): Call<MineUserResponse>
}
