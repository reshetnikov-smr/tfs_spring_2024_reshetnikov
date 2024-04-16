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
        return map {
            val reactionMap: Map<String, Int> =
                it.reactions.groupingBy { it.toEmojiStyle() }.eachCount()
            val checkedSet =
                it.reactions.filter { it.user.id == 709775 }.map { it.toEmojiStyle() }.toSet()

            MessageUiModel(
                messageId = it.id,
                timestamp = it.timestamp.toLong(),
                senderId = it.senderId,
                isMineMessage = it.isMeMessage,
                senderAvatar = it.avatarUrl,
                userName = it.senderFullName,
                message = removeHtmlTags(it.content).replace("\n", "").replace("\r", ""),
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

    private fun frequencyMap(list: List<Int>): MutableMap<Int, Int> {
        val map = mutableMapOf<Int, Int>()
        for (value in list) {
            val count = map.getOrDefault(value, 0)
            map[value] = count + 1
        }
        return map
    }
}
