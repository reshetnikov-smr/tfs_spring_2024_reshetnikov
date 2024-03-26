package ru.elnorte.tfs_spring_2024_reshetnikov

import android.graphics.drawable.Drawable

data class MessageDatabaseModel(
    val messageId: Int,
    val timestamp: Long,
    val senderId: Int,
    val isMineMessage: Boolean,
    val senderAvatar: Drawable?,
    val userName: String?,
    val message: String?,
    // Int - sender Id, String - emoji
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


