package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviIntent

sealed interface TopicIntent : MviIntent {
    data class Init(var channelId: Int, var topicName: String) : TopicIntent
    data object ReloadPage : TopicIntent
    data object LoadMoreItems : TopicIntent
    data class ActionButtonClickIntent(val message: String) : TopicIntent
    data class AddReaction(val messageId: Int, val emoji: String) : TopicIntent
    data class RemoveReaction(val messageId: Int, val emoji: String) : TopicIntent
    data class LongMessageClick(val messageId: Int) : TopicIntent
    data class AddReactionClick(val messageId: Int) : TopicIntent
    data class TextEnteredIntent(val isEmpty: Boolean) : TopicIntent
    data class ListSubmitted(val isSubmitted: Boolean) : TopicIntent
}
