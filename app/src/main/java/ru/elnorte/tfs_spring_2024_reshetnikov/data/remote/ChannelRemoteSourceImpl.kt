package ru.elnorte.tfs_spring_2024_reshetnikov.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApiService
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.StreamsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.SubscribedStreamsResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.TopicsResponse

class ChannelRemoteSourceImpl(private val api: MessengerApiService) : ChannelRemoteSource {

    override suspend fun getStreams(): StreamsResponse {
        return withContext(Dispatchers.IO) {
            checkNotNull(api.getStreams().execute().body()) {
                "getStreams is Null"
            }
        }
    }

    override suspend fun getSubscribedStreams(): SubscribedStreamsResponse {
        return withContext(Dispatchers.IO) {
            checkNotNull(api.getSubscribedStreams().execute().body()) {
                "getSubscribedStreams is Null"
            }
        }
    }

    override suspend fun getTopics(channelId: Int): TopicsResponse {
        return withContext(Dispatchers.IO) {
            checkNotNull(api.getTopics(channelId).execute().body()) {
                "getTopics is Null"
            }
        }
    }
}
