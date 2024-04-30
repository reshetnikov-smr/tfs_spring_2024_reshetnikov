package ru.elnorte.tfs_spring_2024_reshetnikov.data.remote

import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.StreamsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.SubscribedStreamsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.TopicsResponse

interface ChannelRemoteSource {

    suspend fun getStreams(): StreamsResponse
    suspend fun getSubscribedStreams(): SubscribedStreamsResponse
    suspend fun getTopics(channelId: Int): TopicsResponse
}
