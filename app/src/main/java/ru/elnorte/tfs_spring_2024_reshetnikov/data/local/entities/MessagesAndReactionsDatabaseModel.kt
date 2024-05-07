package ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import ru.elnorte.tfs_spring_2024_reshetnikov.hexStringToUnicodeEmoji
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel

data class MessageAndReactionsDatabaseModel(
    @Embedded
    val message: MessageEntity,
    @Relation(
        parentColumn = "message_id",
        entityColumn = "message_id",
    )
    val reactions: List<ReactionEntity>,
)

fun List<MessageAndReactionsDatabaseModel>.asMessageUiModel(): List<MessageUiModel> {
    return this.map { messageAndReactionsDatabaseModel ->
        val reactionMap: Map<String, Int> =
            messageAndReactionsDatabaseModel.reactions.groupingBy { hexStringToUnicodeEmoji(it.emojiCode) }
                .eachCount()
        val checkedSet =
            messageAndReactionsDatabaseModel.reactions.filter { it.senderId == 709775 }
                .map { hexStringToUnicodeEmoji(it.emojiCode) }
                .toSet()
        MessageUiModel(
            messageAndReactionsDatabaseModel.message.messageId,
            messageAndReactionsDatabaseModel.message.timestamp.toLong(),
            messageAndReactionsDatabaseModel.message.senderId,
            messageAndReactionsDatabaseModel.message.senderId == 709775,
            messageAndReactionsDatabaseModel.message.senderAvatar,
            messageAndReactionsDatabaseModel.message.userName,
            messageAndReactionsDatabaseModel.message.content,
            reactionMap,
            checkedSet
        )
    }
}
