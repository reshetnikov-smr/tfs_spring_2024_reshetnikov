package ru.elnorte.tfs_spring_2024_reshetnikov.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.ChannelLocalSource
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.asChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.asTopicUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.asChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.asTopicUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asChannelEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asChannelsDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asSubscribedChannelEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asTopicEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asTopicsDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.remote.ChannelRemoteSource
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IChannelRepository
import javax.inject.Inject

class ChannelRepository @Inject constructor(
    private val remote: ChannelRemoteSource,
    private val local: ChannelLocalSource,
) : IChannelRepository {

    override fun queryChannels(
        queryText: CharSequence,
        isCachedFirst: Boolean,
    ): Flow<List<ChannelUiModel>> {
        return flow {
            if (isCachedFirst) emit(local.getCachedStreams(queryText.toString()).asChannelUiModel())
            val success = remote.getStreams() as? ResponseState.Success
            if (success != null) {
                local.clearCachedStreams()
                local.insertCachedStreams(success.data.asChannelEntity())
                val remoteData = success.data.asChannelsDatabaseModel().filter {
                    it.channelName.contains(queryText) || it.channelDescription.contains(queryText)
                }.asChannelUiModel()
                emit(remoteData)
            } else if (!isCachedFirst) emit(
                local.getCachedStreams(queryText.toString()).asChannelUiModel()
            )
        }
    }

    override fun querySubscribedChannels(
        queryText: CharSequence,
        isCachedFirst: Boolean,
    ): Flow<List<ChannelUiModel>> {
        return flow {
            if (isCachedFirst) emit(
                local.getCachedSubscribedStreams(queryText.toString()).asChannelUiModel()
            )
            val success = remote.getSubscribedStreams() as? ResponseState.Success
            if (success != null) {
                local.clearCachedSubscribedStreams()
                local.insertCachedSubscribedStreams(success.data.asSubscribedChannelEntity())
                val remoteData = success.data.asChannelsDatabaseModel().filter {
                    it.channelName.contains(queryText) || it.channelDescription.contains(queryText)
                }.asChannelUiModel()
                emit(remoteData)
            } else if (!isCachedFirst) {
                emit(local.getCachedSubscribedStreams(queryText.toString()).asChannelUiModel())
            }
        }
    }

    override suspend fun getTopics(
        channelId: Int,
    ): List<TopicUiModel> {
        val response = remote.getTopics(channelId)
        val data = (response as? ResponseState.Success)?.run {
            local.clearTopics(channelId)
            local.insertCachedTopics(data.asTopicEntity(channelId))
            data.asTopicsDatabaseModel().asTopicUiModel(channelId)
        } ?: local.getCachedTopics(channelId).asTopicUiModel()

        return data
    }

    override suspend fun queryCachedChannels(queryText: CharSequence): List<ChannelUiModel> {
        return local.getCachedStreams(queryText.toString()).asChannelUiModel()
    }

    override suspend fun queryCachedSubscribedChannels(queryText: CharSequence): List<ChannelUiModel> {
        return local.getCachedSubscribedStreams(queryText.toString()).asChannelUiModel()
    }

    override suspend fun getCachedTopics(channelId: Int): List<TopicUiModel> {
        return local.getCachedTopics(channelId).asTopicUiModel()
    }
}
