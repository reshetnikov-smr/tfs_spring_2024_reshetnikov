package ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository

import kotlinx.coroutines.flow.Flow
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel

interface IChannelRepository {
    fun queryChannels(
        queryText: CharSequence,
        isCachedFirst: Boolean = false,
    ): Flow<List<ChannelUiModel>>

    fun querySubscribedChannels(
        queryText: CharSequence,
        isCachedFirst: Boolean = false,
    ): Flow<List<ChannelUiModel>>

    suspend fun getTopics(channelId: Int): List<TopicUiModel>


    suspend fun queryCachedChannels(queryText: CharSequence): List<ChannelUiModel>
    suspend fun queryCachedSubscribedChannels(queryText: CharSequence): List<ChannelUiModel>
    suspend fun getCachedTopics(channelId: Int): List<TopicUiModel>
}
