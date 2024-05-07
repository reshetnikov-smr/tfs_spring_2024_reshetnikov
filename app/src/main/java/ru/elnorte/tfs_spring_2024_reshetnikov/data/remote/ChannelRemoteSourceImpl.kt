package ru.elnorte.tfs_spring_2024_reshetnikov.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApiService
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.StreamsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.SubscribedStreamsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.TopicsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState

class ChannelRemoteSourceImpl(private val api: MessengerApiService) : ChannelRemoteSource {

    override suspend fun getStreams(): ResponseState<StreamsResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                checkNotNull(api.getStreams().execute().body()) {
                    "getStreams is Null"
                }
            }.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message.orEmpty()) },
            )
        }
    }

    override suspend fun getSubscribedStreams(): ResponseState<SubscribedStreamsResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                checkNotNull(api.getSubscribedStreams().execute().body()) {
                    "getSubscribedStreams is Null"
                }
            }.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message.orEmpty()) },
            )
        }
    }

    override suspend fun getTopics(channelId: Int): ResponseState<TopicsResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                checkNotNull(api.getTopics(channelId).execute().body()) {
                    "getTopics is Null"
                }
            }.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.message.orEmpty()) },
            )
        }
    }
}
