package ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel

interface IMessengerRepository {
    fun getChannels(isOnlySubscribed: Boolean): List<ChannelUiModel>
    fun getChannelContent(channelId: Int): List<TopicUiModel>
    fun getPeople(): List<PersonUiModel>
    fun getMe(): PersonUiModel
    fun getTopic(topicId: Int): List<MessageUiModel>
    fun toggleReaction(topicId: Int, messageId: Int, emoji: String)
    fun addMessage(topicId: Int, message: String)
}
