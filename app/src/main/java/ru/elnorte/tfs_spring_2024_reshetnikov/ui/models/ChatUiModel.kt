package ru.elnorte.tfs_spring_2024_reshetnikov.ui.models

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic.MessageState

data class ChatUiModel(
    val channelId: Int,
    val topicName: String,
    val messages: List<MessageUiModel>,
    val messageState: MessageState = MessageState.SEND_FILE,
)
