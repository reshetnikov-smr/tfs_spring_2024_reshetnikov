package ru.elnorte.tfs_spring_2024_reshetnikov.data.remote

import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MessageSendingResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MessagesResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState

interface ChatRemoteSource {

    suspend fun sendMessage(
        channelId: Int,
        topicName: String,
        message: String
    ): ResponseState<MessageSendingResponse>

    suspend fun getTopic(
        streamId: Int,
        topicName: String,
        listSize: Int
    ): ResponseState<MessagesResponse>

    suspend fun addReaction(messageId: Int, emoji: String): ResultUiState.Error?
    suspend fun removeReaction(messageId: Int, emoji: String)
}
