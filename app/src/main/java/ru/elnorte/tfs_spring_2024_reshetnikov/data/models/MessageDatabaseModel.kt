package ru.elnorte.tfs_spring_2024_reshetnikov.data.models

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel

/**
 * Message database model
 *
 * @property messageId
 * @property timestamp
 * @property senderId
 * @property isMineMessage
 * @property senderAvatar
 * @property userName
 * @property message
 * @property reactions Int - sender Id, String - emoji
 * @property checkedReaction
 * @constructor Create empty Message database model
 */
data class MessageDatabaseModel(
    val messageId: Int,
    val timestamp: Long,
    val senderId: Int,
    val isMineMessage: Boolean,
    val senderAvatar: Int?,
    val userName: String?,
    val message: String?,
    val reactions: MutableMap<Int, String>,
    val checkedReaction: String?
) {

    fun asMessageUiModel(): MessageUiModel {
        val reactionsCount = mutableMapOf<String, Int>()
        this.reactions.forEach {
            if (reactionsCount.containsKey(it.value)) {
                reactionsCount[it.value] = reactionsCount[it.value]!! + 1
            } else {
                reactionsCount[it.value] = 1
            }
        }
        return MessageUiModel(
            this.messageId,
            this.timestamp,
            this.senderId,
            this.isMineMessage,
            this.senderAvatar,
            this.userName,
            this.message,
            reactionsCount,
            this.checkedReaction
        )
    }
}
