package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviEffect

sealed interface TopicEffect : MviEffect {
    data object DownToList : TopicEffect
    data class ShowError(val throwableMessage: String) : TopicEffect
    data class ShowEmojiDialog(val messageId: Int) : TopicEffect
}
