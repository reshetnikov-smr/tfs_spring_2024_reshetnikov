package ru.elnorte.tfs_spring_2024_reshetnikov.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.dao.ChannelDao
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.dao.ChatDao
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.ChannelEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.MessageEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.ReactionEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.SubscribedChannelEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.TopicEntity

@Database(
    entities = [ChannelEntity::class, SubscribedChannelEntity::class, TopicEntity::class, MessageEntity::class, ReactionEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun channelDao(): ChannelDao
    abstract fun chatDao(): ChatDao
}
