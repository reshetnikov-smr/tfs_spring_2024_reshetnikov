package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChatUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviState

sealed class TopicState : MviState

data class TopicSuccess(val data: ChatUiModel, val showLastMessage: Boolean = false) : TopicState()

data object TopicLoading : TopicState()
