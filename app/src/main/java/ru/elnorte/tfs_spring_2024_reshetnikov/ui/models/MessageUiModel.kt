package ru.elnorte.tfs_spring_2024_reshetnikov.ui.models


data class MessageUiModel(
    val messageId: Int,
    val timestamp: Long,
    val senderId: Int,
    val isMineMessage: Boolean,
    val senderAvatar: String?,
    val userName: String?,
    val message: String?,
    val reactions: Map<String, Int>,
    val checkedReaction: Set<String>,
)
