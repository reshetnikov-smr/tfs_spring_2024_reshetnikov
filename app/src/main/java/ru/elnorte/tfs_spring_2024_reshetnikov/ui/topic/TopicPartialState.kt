package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChatUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviPartialState

sealed interface TopicPartialState : MviPartialState {
    data class DataLoaded(val model: ChatUiModel) : TopicPartialState
    data class MessagesLoaded(val model: List<MessageUiModel>) : TopicPartialState
    data class ActionButtonChanged(val isSend: Boolean) : TopicPartialState
    data object NoChanges : TopicPartialState
    data class MessagesSent(val messages: List<MessageUiModel>) : TopicPartialState
}
