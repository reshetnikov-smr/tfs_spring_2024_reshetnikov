package ru.elnorte.tfs_spring_2024_reshetnikov.data.local

import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.dao.ChatDao
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.MessageAndReactionsDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.MessageEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.ReactionEntity

class ChatLocalSourceImpl(private val dao: ChatDao) : ChatLocalSource {
    override suspend fun insertCachedTopic(
        messages: List<MessageEntity>,
        reactions: List<ReactionEntity>
    ) {
        dao.insertCachedMessages(messages)
        if (messages.isNotEmpty()) {
            val idsForRemoval =
                dao.getOldestMessagesIds(messages[0].channelId, messages[0].parentName.orEmpty())
            dao.clearMessagesByMessageId(idsForRemoval)
            dao.clearReactionsByMessageId(idsForRemoval)
            val ids = dao.getMessagesIdByTopicAndStream(
                messages[0].channelId,
                messages[0].parentName.orEmpty()
            )
            dao.clearReactionsByMessageId(ids)
            dao.insertCachedReactions(reactions)
        }
    }

    override suspend fun getCachedTopic(
        streamId: Int,
        topicName: String
    ): List<MessageAndReactionsDatabaseModel> {
        return dao.getTopic(streamId, topicName)
    }

    override suspend fun clearCachedTopic(streamId: Int, topicName: String) {
        val idsForRemoval = dao.getMessagesIdByTopicAndStream(streamId, topicName)
        dao.clearMessagesByMessageId(idsForRemoval)
        dao.clearReactionsByMessageId(idsForRemoval)
    }
}
