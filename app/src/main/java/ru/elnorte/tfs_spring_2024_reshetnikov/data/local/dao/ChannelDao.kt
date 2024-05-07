package ru.elnorte.tfs_spring_2024_reshetnikov.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.ChannelEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.SubscribedChannelEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.TopicEntity

@Dao
interface ChannelDao {
    @Query("DELETE FROM channels_table")
    suspend fun deleteAllCachedStreams()

    @Query("DELETE FROM subscribed_channels_table")
    suspend fun deleteAllCachedSubscribedStreams()

    @Query("DELETE FROM topics_table WHERE parent_id=:parentId")
    suspend fun deleteAllTopics(parentId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedStream(stream: ChannelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedSubscribedStream(stream: SubscribedChannelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedTopic(stream: TopicEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedStreams(stream: List<ChannelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedSubscribedStreams(stream: List<SubscribedChannelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedTopics(stream: List<TopicEntity>)


    @Query("SELECT * FROM channels_table WHERE name LIKE '%' || :text || '%' OR description LIKE '%' || :text || '%' ORDER BY channel_id DESC")
    suspend fun getCachedStreams(text: String): List<ChannelEntity>

    @Query("SELECT * FROM subscribed_channels_table WHERE name LIKE '%' || :text || '%' OR description LIKE '%' || :text || '%' ORDER BY channel_id DESC")
    suspend fun getCachedSubscribedStreams(text: String): List<SubscribedChannelEntity>

    @Query("SELECT * FROM topics_table WHERE parent_id=:channelId ORDER BY name DESC")
    suspend fun getCachedTopics(channelId: Int): List<TopicEntity>
}
