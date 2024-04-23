package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChatUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviState

data class TopicState(val topicUi: TopicUiState<ChatUiModel>) : MviState
