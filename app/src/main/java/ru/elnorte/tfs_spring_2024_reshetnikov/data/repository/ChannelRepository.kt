package ru.elnorte.tfs_spring_2024_reshetnikov.data.repository

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.asChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.asTopicUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApi
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.GenericResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asChannelsDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asTopicsDatabaseModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IChannelRepository

class ChannelRepository(
    private val messageConverter: MessageConverter,
) : IChannelRepository {
    override suspend fun queryChannels(queryText: CharSequence): List<ChannelUiModel> {
        return withContext(Dispatchers.IO) {
            val data = MessengerApi.retrofitService.getStreams()
            data.asChannelsDatabaseModel().filter {
                it.channelName.contains(queryText) || it.channelDescription.contains(queryText)
            }.asChannelUiModel()
        }
    }

    override suspend fun querySubscribedChannels(queryText: CharSequence): List<ChannelUiModel> {
        return withContext(Dispatchers.IO) {
            val data = MessengerApi.retrofitService.getSubscribedStreams()
            data.asChannelsDatabaseModel().filter {
                it.channelName.contains(queryText) || it.channelDescription.contains(queryText)
            }.asChannelUiModel()
        }
    }

    override suspend fun getTopics(channelId: Int): List<TopicUiModel> {
        return withContext(Dispatchers.IO) {
            val data = MessengerApi.retrofitService.getTopics(channelId)
            data.asTopicsDatabaseModel().asTopicUiModel(channelId)
        }
    }


    override suspend fun getTopic(
        streamId: Int,
        topicName: String,
    ): List<MessageUiModel> {
        return withContext(Dispatchers.IO) {
            val narrowJson =
                "[{\"negated\":false,\"operator\":\"stream\",\"operand\":$streamId},{\"negated\":false,\"operator\":\"topic\",\"operand\":\"$topicName\"}]"


            val call = MessengerApi.retrofitService.getMessages(
                "newest",
                100,
                0,
                narrowJson
            )
            val response = call.execute()

            if (response.isSuccessful) {
                val data = response.body()
                data?.run { messageConverter.convert(messages) } ?: emptyList()
            } else {
                emptyList()
            }
        }
    }

    override suspend fun sendMessage(channelId: Int, topicName: String, message: String) {
        withContext(Dispatchers.IO) {
            val call = MessengerApi.retrofitService.sendStreamMessage(
                to = channelId.toString(), topic = topicName, content = message
            )
            call.execute()
        }
    }

    override suspend fun addReaction(
        messageId: Int,
        emoji: String,
    ): ResultUiState.Error? {
        return withContext(Dispatchers.IO) {
            handleReactionResponse(messageId, emoji)
        }
    }

    private fun handleReactionResponse(messageId: Int, emoji: String): ResultUiState.Error? {
        return runCatching {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter: JsonAdapter<GenericResponse> =
                moshi.adapter(GenericResponse::class.java)

            val call = MessengerApi.retrofitService.addEmoji(messageId, emoji)
            val response = call.execute()

            return if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val errorResponse = jsonAdapter.fromJson(errorBody)
                ResultUiState.Error(errorResponse?.msg.orEmpty())
            } else {
                null
            }
        }.getOrNull()
    }

    override suspend fun removeReaction(messageId: Int, emoji: String) {
        withContext(Dispatchers.IO) {
            MessengerApi.retrofitService.removeEmoji(messageId, emoji)
        }
    }
}
