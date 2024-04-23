package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicTransferModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviEffect

sealed interface PageEffect : MviEffect {
    data class ShowError(val throwable: Throwable) : PageEffect
    data class NavigateToChat(val topicTransferModel: TopicTransferModel) : PageEffect
}
