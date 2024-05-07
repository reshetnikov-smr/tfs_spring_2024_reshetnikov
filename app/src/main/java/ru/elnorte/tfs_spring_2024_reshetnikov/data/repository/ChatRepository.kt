import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.elnorte.tfs_spring_2024_reshetnikov.data.convert.MessageConverter
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.ChatLocalSource
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities.asMessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MessageSendingResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asMessageEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asReactionEntity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.remote.ChatRemoteSource
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IChatRepository
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val messageConverter: MessageConverter,
    private val remote: ChatRemoteSource,
    private val local: ChatLocalSource,
) : IChatRepository {

    override suspend fun getTopic(
        streamId: Int,
        topicName: String,
        listSize: Int,
        isCachedFirst: Boolean,
    ): Flow<ResponseState<List<MessageUiModel>>> = flow {
        val localData = local.getCachedTopic(streamId, topicName).asMessageUiModel()
        if (isCachedFirst) emit(ResponseState.Success(localData))
        when (val messagesResponse = remote.getTopic(streamId, topicName, listSize)) {
            is ResponseState.Error -> emit(ResponseState.Error(messagesResponse.errorMessage))
            is ResponseState.Success -> {
                local.insertCachedTopic(
                    messagesResponse.data.asMessageEntity(topicName, streamId),
                    messagesResponse.data.asReactionEntity()
                )
                val successData = messageConverter.convert(messagesResponse.data.messages)
                emit(ResponseState.Success(successData))
            }
        }
    }

    override suspend fun sendMessage(
        channelId: Int,
        topicName: String,
        message: String,
    ): ResponseState<MessageSendingResponse> {
        return remote.sendMessage(channelId, topicName, message)
    }

    override suspend fun addReaction(
        messageId: Int,
        emoji: String,
    ): ResultUiState.Error? {
        return remote.addReaction(messageId, emoji)
    }

    override suspend fun removeReaction(messageId: Int, emoji: String) {
        remote.removeReaction(messageId, emoji)
    }

    override suspend fun getCachedTopic(streamId: Int, topicName: String): List<MessageUiModel> {
        return local.getCachedTopic(streamId, topicName).asMessageUiModel()
    }
}
