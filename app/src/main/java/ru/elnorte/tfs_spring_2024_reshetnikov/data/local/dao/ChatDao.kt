package ru.elnorte.tfs_spring_2024_reshetnikov.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.MessageAndReactionsDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.MessageEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.ReactionEntity

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedReactions(reactions: List<ReactionEntity>)

    @Query("SELECT message_id FROM messages_table WHERE channel_id=:streamId AND parent_name=:topicName ORDER BY timestamp DESC LIMIT -1 OFFSET :limit")
    suspend fun getOldestMessagesIds(streamId: Int, topicName: String, limit: Int = 50): List<Int>

    @Query("DELETE FROM messages_table WHERE message_id IN (:messagesId)")
    suspend fun clearMessagesByMessageId(messagesId: List<Int>) //

    @Query("DELETE FROM reactions_table WHERE message_id IN (:messagesId)")
    suspend fun clearReactionsByMessageId(messagesId: List<Int>)

    @Query("SELECT message_id FROM messages_table WHERE channel_id=:streamId AND parent_name=:topicName")
    suspend fun getMessagesIdByTopicAndStream(streamId: Int, topicName: String): List<Int>

    @Transaction
    @Query("SELECT * FROM messages_table WHERE channel_id=:streamId AND parent_name=:topicName")
    suspend fun getTopic(streamId: Int, topicName: String): List<MessageAndReactionsDatabaseModel>

}
