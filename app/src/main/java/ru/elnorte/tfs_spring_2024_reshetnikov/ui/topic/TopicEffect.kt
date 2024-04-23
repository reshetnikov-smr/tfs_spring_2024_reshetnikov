package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviEffect

sealed interface TopicEffect : MviEffect {
    data class ShowError(val throwable: Throwable) : TopicEffect
    data class ShowEmojiDialog(val messageId: Int) : TopicEffect
    data object NavigateBack : TopicEffect

}
