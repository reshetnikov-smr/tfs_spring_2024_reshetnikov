package ru.elnorte.tfs_spring_2024_reshetnikov.ui.models


/**
 * Message ui model
 *
 * @property messageId
 * @property timestamp
 * @property senderId
 * @property isMineMessage
 * @property senderAvatar
 * @property userName
 * @property message
 * @property reactions String - emoji, Int - count
 * @property checkedReaction
 * @constructor Create empty Message ui model
 */
data class MessageUiModel(
    val messageId: Int,
    val timestamp: Long,
    val senderId: Int,
    val isMineMessage: Boolean,
    val senderAvatar: Int?,
    val userName: String?,
    val message: String?,
    val reactions: Map<String, Int>,
    val checkedReaction: String?
)
