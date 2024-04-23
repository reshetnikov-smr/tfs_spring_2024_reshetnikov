package ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel

interface IChannelRepository {
    suspend fun queryChannels(queryText: CharSequence): List<ChannelUiModel>
    suspend fun querySubscribedChannels(queryText: CharSequence): List<ChannelUiModel>
    suspend fun getTopics(channelId: Int): List<TopicUiModel>
    suspend fun getTopic(streamId: Int, topicName: String): List<MessageUiModel>
    suspend fun sendMessage(channelId: Int, topicName: String, message: String)
    suspend fun addReaction(messageId: Int, emoji: String): ResultUiState.Error?
    suspend fun removeReaction(messageId: Int, emoji: String)
}
