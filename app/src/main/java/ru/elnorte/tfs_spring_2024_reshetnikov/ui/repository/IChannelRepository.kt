package ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel

interface IChannelRepository {
    suspend fun queryChannels(queryText: CharSequence): List<ChannelUiModel>
    suspend fun querySubscribedChannels(queryText: CharSequence): List<ChannelUiModel>
    suspend fun getTopics(channelId: Int): List<TopicUiModel>
}
