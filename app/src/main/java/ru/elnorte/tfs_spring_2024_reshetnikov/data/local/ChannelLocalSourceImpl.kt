package ru.elnorte.tfs_spring_2024_reshetnikov.data.local

import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.dao.ChannelDao
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.ChannelEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.SubscribedChannelEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.TopicEntity

class ChannelLocalSourceImpl(private val dao: ChannelDao) : ChannelLocalSource {
    override suspend fun getCachedStreams(text: String): List<ChannelEntity> {
        return dao.getCachedStreams(text)
    }

    override suspend fun getCachedSubscribedStreams(text: String): List<SubscribedChannelEntity> {
        return dao.getCachedSubscribedStreams(text)
    }

    override suspend fun getCachedTopics(channelId: Int): List<TopicEntity> {
        return dao.getCachedTopics(channelId)
    }

    override suspend fun clearCachedStreams() {
        dao.deleteAllCachedStreams()
    }

    override suspend fun clearCachedSubscribedStreams() {
        dao.deleteAllCachedSubscribedStreams()
    }

    override suspend fun clearTopics(parentId: Int) {
        dao.deleteAllTopics(parentId)
    }

    override suspend fun insertCachedStream(model: ChannelEntity) {
        dao.insertCachedStream(model)
    }

    override suspend fun insertCachedSubscribedStream(model: SubscribedChannelEntity) {
        dao.insertCachedSubscribedStream(model)
    }

    override suspend fun insertCachedTopic(model: TopicEntity) {
        dao.insertCachedTopic(model)
    }

    override suspend fun insertCachedStreams(model: List<ChannelEntity>) {
        dao.insertCachedStreams(model)
    }

    override suspend fun insertCachedSubscribedStreams(model: List<SubscribedChannelEntity>) {
        dao.insertCachedSubscribedStreams(model)
    }

    override suspend fun insertCachedTopics(model: List<TopicEntity>) {
        dao.insertCachedTopics(model)
    }
}
