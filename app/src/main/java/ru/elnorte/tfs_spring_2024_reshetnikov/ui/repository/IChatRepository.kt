package ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository

import kotlinx.coroutines.flow.Flow
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MessageSendingResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState

interface IChatRepository {
    suspend fun getTopic(
        streamId: Int,
        topicName: String,
        listSize: Int,
        isCachedFirst: Boolean = false,
    ): Flow<ResponseState<List<MessageUiModel>>>

    suspend fun sendMessage(
        channelId: Int,
        topicName: String,
        message: String,
    ): ResponseState<MessageSendingResponse>

    suspend fun addReaction(messageId: Int, emoji: String): ResultUiState.Error?
    suspend fun removeReaction(messageId: Int, emoji: String)

    suspend fun getCachedTopic(streamId: Int, topicName: String): List<MessageUiModel>
}
