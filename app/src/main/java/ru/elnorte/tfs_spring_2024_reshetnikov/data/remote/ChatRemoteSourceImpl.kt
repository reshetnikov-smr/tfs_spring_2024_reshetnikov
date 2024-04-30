package ru.elnorte.tfs_spring_2024_reshetnikov.data.remote

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApiService
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.GenericResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MessageResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MessageSendingResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState

class ChatRemoteSourceImpl(private val api: MessengerApiService) : ChatRemoteSource {

    override suspend fun getTopic(streamId: Int, topicName: String): List<MessageResponse> {
        return withContext(Dispatchers.IO) {
            val narrowJson =
                "[{\"negated\":false,\"operator\":\"stream\",\"operand\":$streamId},{\"negated\":false,\"operator\":\"topic\",\"operand\":\"$topicName\"}]"

            val call = api.getMessages(
                "newest",
                100,
                0,
                narrowJson
            )
            val response = call.execute()

            response.body()?.messages.orEmpty()
        }
    }

    override suspend fun sendMessage(
        channelId: Int,
        topicName: String,
        message: String,
    ): ResponseState<MessageSendingResponse> =
        withContext(Dispatchers.IO) {
            runCatching {
                val call = api.sendStreamMessage(
                    to = channelId.toString(), topic = topicName, content = message
                )
                checkNotNull(call.execute().body()) {
                    "empty MessageResponse"
                }
            }.fold(
                onSuccess = { ResponseState.Success(it) },
                onFailure = { ResponseState.Error(it.stackTraceToString()) }
            )
        }

    override suspend fun addReaction(messageId: Int, emoji: String): ResultUiState.Error? {
        return withContext(Dispatchers.IO) {
            handleReactionResponse(messageId, emoji)
        }
    }

    override suspend fun removeReaction(messageId: Int, emoji: String) {
        return withContext(Dispatchers.IO) {
            val call = api.removeEmoji(messageId, emoji)
            call.execute()
        }
    }


    private fun handleReactionResponse(messageId: Int, emoji: String): ResultUiState.Error? {
        return runCatching {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter: JsonAdapter<GenericResponse> =
                moshi.adapter(GenericResponse::class.java)

            val call = api.addEmoji(messageId, emoji)
            val response = call.execute()

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val errorResponse = jsonAdapter.fromJson(errorBody)
                ResultUiState.Error(errorResponse?.msg.orEmpty())
            } else {
                null
            }
        }.getOrNull()
    }
}
