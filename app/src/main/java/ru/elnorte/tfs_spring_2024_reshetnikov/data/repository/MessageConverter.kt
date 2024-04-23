package ru.elnorte.tfs_spring_2024_reshetnikov.data.repository

import android.text.Html
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MessageResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.ReactionResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel

class MessageConverter {
    fun convert(remote: List<MessageResponse>): List<MessageUiModel> {
        return remote.toDomain()
    }

    private fun List<MessageResponse>.toDomain(): List<MessageUiModel> {
        return map { messageResponse ->
            val reactionMap: Map<String, Int> =
                messageResponse.reactions.groupingBy { it.toEmojiStyle() }.eachCount()
            val checkedSet =
                messageResponse.reactions.filter { it.user.id == 709775 }.map { it.toEmojiStyle() }
                    .toSet()

            MessageUiModel(
                messageId = messageResponse.id,
                timestamp = messageResponse.timestamp.toLong(),
                senderId = messageResponse.senderId,
                isMineMessage = messageResponse.senderId == 709775,
                senderAvatar = messageResponse.avatarUrl,
                userName = messageResponse.senderFullName,
                message = removeHtmlTags(messageResponse.content).replace("\n", "")
                    .replace("\r", ""),
                reactions = reactionMap,
                checkedReaction = checkedSet
            )
        }
    }

    private fun ReactionResponse.toEmojiStyle(): String =
        when (reactionType) {
            "unicode_emoji" -> {
                if (!emojiCode.contains("-")) {
                    //emojiName
                    String(Character.toChars(emojiCode.toInt(radix = 16)))
                } else {
                    ""
                }
            }

            else -> {
                ""
            }
        }

    private fun removeHtmlTags(input: String): String {
        return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY).toString()
            .replace(Regex("<.*?>"), "")
    }
}
