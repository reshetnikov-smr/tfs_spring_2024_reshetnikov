package ru.elnorte.tfs_spring_2024_reshetnikov.data.remote

import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.StreamsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.SubscribedStreamsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.TopicsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState

interface ChannelRemoteSource {

    suspend fun getStreams(): ResponseState<StreamsResponse>
    suspend fun getSubscribedStreams(): ResponseState<SubscribedStreamsResponse>
    suspend fun getTopics(channelId: Int): ResponseState<TopicsResponse>
}
