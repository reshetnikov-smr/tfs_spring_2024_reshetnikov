package ru.elnorte.tfs_spring_2024_reshetnikov

import android.graphics.drawable.Drawable

data class MessageUiModel(
    val messageId: Int,
    val timestamp: Long,
    val senderId: Int,
    val isMineMessage: Boolean,
    val senderAvatar: Drawable?,
    val userName: String?,
    val message: String?,
    //String - emoji, Int - count
    val reactions: Map<String, Int>,
    val checkedReaction: String?
)

