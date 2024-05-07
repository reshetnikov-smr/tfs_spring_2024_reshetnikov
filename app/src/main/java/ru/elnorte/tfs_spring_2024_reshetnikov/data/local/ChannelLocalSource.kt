package ru.elnorte.tfs_spring_2024_reshetnikov.data.local

import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.ChannelEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.SubscribedChannelEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.TopicEntity

interface ChannelLocalSource {
    suspend fun getCachedStreams(text: String): List<ChannelEntity>
    suspend fun getCachedSubscribedStreams(text: String): List<SubscribedChannelEntity>
    suspend fun getCachedTopics(channelId: Int): List<TopicEntity>
    suspend fun clearCachedStreams()
    suspend fun clearCachedSubscribedStreams()
    suspend fun clearTopics(parentId: Int)
    suspend fun insertCachedStream(model: ChannelEntity)
    suspend fun insertCachedSubscribedStream(model: SubscribedChannelEntity)
    suspend fun insertCachedTopic(model: TopicEntity)
    suspend fun insertCachedStreams(model: List<ChannelEntity>)
    suspend fun insertCachedSubscribedStreams(model: List<SubscribedChannelEntity>)
    suspend fun insertCachedTopics(model: List<TopicEntity>)
}
