package ru.elnorte.tfs_spring_2024_reshetnikov

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources

class MockRepository(context: Context) {
    private var myId = 2
    private var myAvatar = AppCompatResources.getDrawable(context, R.drawable.huge_avatar)
    private var myUsername = "Алексей Решетников"
    private var conversation = mutableListOf(
        MessageDatabaseModel(
            1,
            1711348699,
            1,
            false,
            AppCompatResources.getDrawable(context, R.drawable.redhead_avatar),
            "Елена Игнатьева",
            "Да я думаю, купить билеты нереально",
            mutableMapOf(), null
        ),
//        MessageUiModel(2, 1711348707, 1, false, null, "Елена Игнатьева", "Все пишут", null),
        MessageDatabaseModel(
            3,
            1711348715,
            1,
            false,
            AppCompatResources.getDrawable(context, R.drawable.redhead_avatar),
            "Елена Игнатьева",
            "Просто хочу попробовать",
            mutableMapOf(2 to "❤", 3 to "\uD83D\uDE03"), "❤"
        ),
        MessageDatabaseModel(
            4,
            1711348739,
            2,
            true,
            AppCompatResources.getDrawable(context, R.drawable.huge_avatar),
            "Алексей Решетников",
            "а где покупать?",
            mutableMapOf(), null
        ),
        MessageDatabaseModel(
            5,
            1711348799,
            1,
            false,
            AppCompatResources.getDrawable(context, R.drawable.redhead_avatar),
            "Елена Игнатьева",
            "Там ссылка",
            mutableMapOf(2 to "\uD83D\uDC40"), "\uD83D\uDC40"
        ),
//        MessageUiModel(
//            6,
//            1711348831,
//            1,
//            false,
//            AppCompatResources.getDrawable(context, R.drawable.redhead_avatar),
//            "Елена Игнатьева",
//            "Завтра в 12 начинается продажа",
//            null
//        ),
        MessageDatabaseModel(
            7,
            1711348851,
            2,
            true,
            AppCompatResources.getDrawable(context, R.drawable.huge_avatar),
            "Алексей Решетников",
            "а, вижу",
            mutableMapOf(), null
        ),
        MessageDatabaseModel(
            8,
            1711473902,
            2,
            true,
            AppCompatResources.getDrawable(context, R.drawable.huge_avatar),
            "Алексей Решетников",
            "напомни только, а то я забуду",
            mutableMapOf(1 to "\uD83D\uDE03", 3 to "\uD83D\uDE03"), null
        ),
    )

    fun getConversation(): List<MessageUiModel> {
        return conversation.map { it.asMessageUiModel() }
    }

    fun sendMessage(message: String) {
        conversation.add(
            MessageDatabaseModel(
                conversation.last().messageId + 1,
                getCurrentDateAndTime(),
                myId,
                true,
                myAvatar,
                myUsername,
                message,
                mutableMapOf(),
                null
            )
        )
    }

    fun toggleReaction(messageId: Int, emoji: String) {
        val message = conversation.find { it.messageId == messageId }
        if (message?.reactions?.containsKey(myId) == true) {
            if (message.reactions[myId] == emoji) {
                message.reactions.remove(myId)

                conversation[conversation.indexOf(message)] = message.copy(checkedReaction = null)
            }

        } else {
            message?.reactions?.put(myId, emoji)
            if (message != null) {
                conversation[conversation.indexOf(message)] = message.copy(checkedReaction = emoji)
            }
        }
        conversation.find { it.messageId == messageId }
    }
}
