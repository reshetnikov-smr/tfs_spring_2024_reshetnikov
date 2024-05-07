package ru.elnorte.tfs_spring_2024_reshetnikov.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.AppDatabase
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.dao.ChannelDao
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.dao.ChatDao
import javax.inject.Singleton


@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()

    }

    @Provides
    @Singleton
    fun provideChannelDao(database: AppDatabase): ChannelDao = database.channelDao()

    @Provides
    @Singleton
    fun provideChatDao(database: AppDatabase): ChatDao = database.chatDao()

}
