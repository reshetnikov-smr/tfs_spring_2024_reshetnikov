package ru.elnorte.tfs_spring_2024_reshetnikov.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.API_KEY
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.AUTH_MAIL
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApiService
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.BASE_URL
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitService(
        internetClient: OkHttpClient,
        moshi: Moshi,
    ): MessengerApiService {
        return Retrofit.Builder()
            .client(internetClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val credentials = Credentials.basic(AUTH_MAIL, API_KEY)
            val newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", credentials)
                .build()
            chain.proceed(newRequest)
        }

        return OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .build()
    }
}
