package ru.elnorte.tfs_spring_2024_reshetnikov.data.local

import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.MessageAndReactionsDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.MessageEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.ReactionEntity

interface ChatLocalSource {

    suspend fun insertCachedTopic(messages: List<MessageEntity>, reactions: List<ReactionEntity>)
    suspend fun getCachedTopic(
        streamId: Int,
        topicName: String
    ): List<MessageAndReactionsDatabaseModel>

    suspend fun clearCachedTopic(streamId: Int, topicName: String)
}
