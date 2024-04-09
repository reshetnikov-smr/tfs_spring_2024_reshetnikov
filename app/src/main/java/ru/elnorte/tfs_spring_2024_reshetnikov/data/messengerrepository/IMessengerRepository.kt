package ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel

interface IMessengerRepository {
    suspend fun getChannelContent(channelId: Int): List<TopicUiModel>

    suspend fun getPeople(): List<PersonUiModel>

    suspend fun getMe(): PersonUiModel

    suspend fun getTopic(topicId: Int): List<MessageUiModel>

    suspend fun toggleReaction(topicId: Int, messageId: Int, emoji: String)

    suspend fun addMessage(topicId: Int, message: String)

    suspend fun queryChannels(queryText: CharSequence): List<ChannelUiModel>

    suspend fun querySubscribedChannels(queryText: CharSequence): List<ChannelUiModel>

    suspend fun queryContacts(queryText: String): List<PersonUiModel>

}
