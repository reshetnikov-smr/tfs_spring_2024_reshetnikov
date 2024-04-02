package ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository

import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.ChannelDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.MessageDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.TopicDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.asChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.asTopicUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.getCurrentDateAndTime
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel

class FakeRepository : IMessengerRepository {
    private var myId = 2
    private var myAvatar = R.drawable.huge_avatar
    private var myUsername = "Алексей Решетников"
    private val subscribedChannels = listOf(10000, 10002)
    private val topicsList = listOf(
        TopicDatabaseModel(
            1,
            "Testing",
            1240,
            "#2A9D8F",
        ),
        TopicDatabaseModel(
            2,
            "Bruh",
            24,
            "#E9C46A",
        ),
        TopicDatabaseModel(
            3,
            "Jetpack compose migration",
            1182367,
            "#4CAF50",
        ),
        TopicDatabaseModel(
            4,
            "RecyclerView",
            5789,
            "#455A64",
        ),
        TopicDatabaseModel(
            5,
            "Figma or Paint?",
            7,
            "#E64A19",
        ),
        TopicDatabaseModel(
            6,
            "Сомнительно, но ОКЭЙ",
            7,
            "#03A9f4",
        ),
        TopicDatabaseModel(
            7,
            "Naming holywar",
            329,
            "#E91E63",
        ),
    )
    private val channelsList = listOf(
        ChannelDatabaseModel(
            10000,
            "#general",
            "general",
            listOf(1, 2),
        ),
        ChannelDatabaseModel(
            10001,
            "#Developement",
            "developement",
            listOf(3, 4),
        ),
        ChannelDatabaseModel(
            10002,
            "#Design",
            "design",
            listOf(5, 7),
        ),
        ChannelDatabaseModel(
            10003,
            "#PR",
            "pr",
            listOf(6),
        ),
        ChannelDatabaseModel(
            10004,
            "#No Topics",
            "test no topics",
            emptyList(),
        ),
    )
    private var conversation = mutableListOf(
        MessageDatabaseModel(
            1,
            1711348699,
            1,
            false,
            R.drawable.redhead_avatar,
            "Елена Игнатьева",
            "Да я думаю, купить билеты нереально",
            mutableMapOf(), null
        ),
        MessageDatabaseModel(
            2,
            1711348707,
            1,
            false,
            R.drawable.redhead_avatar,
            "Елена Игнатьева",
            "Все пишут",
            mutableMapOf(),
            null
        ),
        MessageDatabaseModel(
            3,
            1711348715,
            1,
            false,
            R.drawable.redhead_avatar,
            "Елена Игнатьева",
            "Просто хочу попробовать",
            mutableMapOf(2 to "❤", 3 to "\uD83D\uDE03"), "❤"
        ),
        MessageDatabaseModel(
            4,
            1711348739,
            2,
            true,
            R.drawable.huge_avatar,
            "Алексей Решетников",
            "а где покупать?",
            mutableMapOf(), null
        ),
        MessageDatabaseModel(
            5,
            1711348799,
            1,
            false,
            R.drawable.redhead_avatar,
            "Елена Игнатьева",
            "Там ссылка",
            mutableMapOf(2 to "\uD83D\uDC40"), "\uD83D\uDC40"
        ),
        MessageDatabaseModel(
            6,
            1711348831,
            1,
            false,
            R.drawable.redhead_avatar,
            "Елена Игнатьева",
            "Завтра в 12 начинается продажа",
            mutableMapOf(), null
        ),
        MessageDatabaseModel(
            7,
            1711348851,
            2,
            true,
            R.drawable.huge_avatar,
            "Алексей Решетников",
            "а, вижу",
            mutableMapOf(), null
        ),
        MessageDatabaseModel(
            8,
            1711473902,
            2,
            true,
            R.drawable.huge_avatar,
            "Алексей Решетников",
            "напомни только, а то я забуду",
            mutableMapOf(1 to "\uD83D\uDE03", 3 to "\uD83D\uDE03"), null
        ),
    )
    private val topicsContent = mutableMapOf(
        1 to conversation,
        2 to mutableListOf(
            MessageDatabaseModel(
                126,
                1711348699,
                1,
                false,
                R.drawable.redhead_avatar,
                "Елена Игнатьева",
                "bruh topic",
                mutableMapOf(), null
            )
        ),
        3 to mutableListOf(
            MessageDatabaseModel(
                126,
                1711348699,
                1,
                false,
                R.drawable.redhead_avatar,
                "Елена Игнатьева",
                "jetpack compose migration topic",
                mutableMapOf(), null
            )
        ),
        4 to mutableListOf(
            MessageDatabaseModel(
                126,
                1711348699,
                1,
                false,
                R.drawable.redhead_avatar,
                "Елена Игнатьева",
                "recyclerview topic",
                mutableMapOf(), null
            )
        ),
        5 to mutableListOf(
            MessageDatabaseModel(
                126,
                1711348699,
                1,
                false,
                R.drawable.redhead_avatar,
                "Елена Игнатьева",
                "figma or paint topic",
                mutableMapOf(), null
            )
        ),
        6 to mutableListOf(
            MessageDatabaseModel(
                126,
                1711348699,
                1,
                false,
                R.drawable.redhead_avatar,
                "Елена Игнатьева",
                "oleg topic",
                mutableMapOf(), null
            )
        ),
        7 to mutableListOf(
            MessageDatabaseModel(
                126,
                1711348699,
                1,
                false,
                R.drawable.redhead_avatar,
                "Елена Игнатьева",
                "naming topic",
                mutableMapOf(), null
            )
        ),
    )

    override fun getChannels(isOnlySubscribed: Boolean): List<ChannelUiModel> {
        return if (isOnlySubscribed) {
            channelsList.filter { subscribedChannels.contains(it.channelId) }
                .map { it.asChannelUiModel() }
        } else {
            channelsList.map { it.asChannelUiModel() }
        }

    }

    override fun getChannelContent(channelId: Int): List<TopicUiModel> {
        val output = mutableListOf<TopicUiModel>()
        channelsList.first { it.channelId == channelId }.topicsList.forEach { topicIndex ->
            output.add(topicsList.first { it.topicId == topicIndex }.asTopicUiModel())
        }
        return output.toList()
    }

    override fun getPeople(): List<PersonUiModel> {
        val personsList = List(15) {
            PersonUiModel(
                1,
                "Елена Игнатьева",
                "helen@company.name",
                "On the road again..",
                "online",
                R.drawable.redhead_avatar,
            )
        }
//        return listOf(
//            PersonUiModel(
//                1,
//                "Елена Игнатьева",
//                "helen@company.name",
//                "On the road again..",
//                "online",
//                R.drawable.redhead_avatar,
//            ),
//            PersonUiModel(
//                2,
//                "Алексей Решетников",
//                "alex@company.name",
//                "Coding..",
//                "online",
//                R.drawable.huge_avatar,
//
//            ),
//        )
        return personsList
    }

    override fun getMe(): PersonUiModel {
        return PersonUiModel(
            14327,
            "Darrell Steward",
            "darell@company.name",
            "In a meeting",
            "online",
            R.drawable.regular_avatar,
        )
    }

    override fun getTopic(topicId: Int): List<MessageUiModel> {
        return topicsContent[topicId]?.map { it.asMessageUiModel() } ?: emptyList()
    }

    override fun toggleReaction(topicId: Int, messageId: Int, emoji: String) {
        val chat = topicsContent[topicId]
        val message = chat?.find { it.messageId == messageId }
        var messageIndex: Int?
        message.let {
            messageIndex = chat?.indexOf(message)
        }
        if (message?.reactions?.containsKey(myId) == true) {
            if (message.reactions[myId] == emoji) {
                message.reactions.remove(myId)
                topicsContent[topicId]?.set(
                    chat.indexOf(message),
                    message.copy(checkedReaction = null)
                )
            }
        } else {
            message?.reactions?.put(myId, emoji)
            if (message != null) {
                messageIndex?.let {
                    topicsContent[topicId]?.set(
                        it,
                        message.copy(checkedReaction = emoji)
                    )
                }
            }
        }

        // topicsContent[topicId]?.find { it.messageId == messageId }
    }

    override fun addMessage(topicId: Int, message: String) {
        topicsContent[topicId]?.add(
            MessageDatabaseModel(
                topicsContent[topicId]?.last()?.messageId?.plus(1) ?: 1,
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
}
