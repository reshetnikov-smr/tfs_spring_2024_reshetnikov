package ru.elnorte.tfs_spring_2024_reshetnikov.data.repository

import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.asChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.asTopicUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asChannelsDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asTopicsDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.remote.ChannelRemoteSource
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IChannelRepository
import javax.inject.Inject

class ChannelRepository @Inject constructor(
    private val remote: ChannelRemoteSource,
) : IChannelRepository {

    override suspend fun queryChannels(queryText: CharSequence): List<ChannelUiModel> {
        return remote.getStreams().run {
            asChannelsDatabaseModel().filter {
                it.channelName.contains(queryText) || it.channelDescription.contains(queryText)
            }.asChannelUiModel()
        }
    }

    override suspend fun querySubscribedChannels(queryText: CharSequence): List<ChannelUiModel> {
        return remote.getSubscribedStreams().run {
            asChannelsDatabaseModel().filter {
                it.channelName.contains(queryText) || it.channelDescription.contains(queryText)
            }.asChannelUiModel()
        }
    }

    override suspend fun getTopics(channelId: Int): List<TopicUiModel> {
        return remote.getTopics(channelId).run {
            asTopicsDatabaseModel().asTopicUiModel(channelId)
        }
    }
}
