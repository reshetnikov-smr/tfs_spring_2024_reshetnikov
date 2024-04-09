package ru.elnorte.tfs_spring_2024_reshetnikov.data.models

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel

data class MessageDatabaseModel(
    val messageId: Int,
    val timestamp: Long,
    val senderId: Int,
    val isMineMessage: Boolean,
    val senderAvatar: Int?,
    val userName: String?,
    val message: String?,
    val reactions: MutableMap<Int, MutableSet<String>>,
    val checkedReaction: Set<String>
) {

    fun asMessageUiModel(): MessageUiModel {
        val reactionsCount = mutableMapOf<String, Int>()
        this.reactions.forEach { user ->
            user.value.forEach {
                if (reactionsCount.containsKey(it)) {
                    reactionsCount[it] = reactionsCount[it]!! + 1
                } else {
                    reactionsCount[it] = 1
                }
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
