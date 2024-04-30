import ru.elnorte.tfs_spring_2024_reshetnikov.data.convert.MessageConverter
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MessageSendingResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.remote.ChatRemoteSource
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IChatRepository
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val messageConverter: MessageConverter,
    private val remote: ChatRemoteSource,
) : IChatRepository {

    override suspend fun getTopic(
        streamId: Int,
        topicName: String,
    ): List<MessageUiModel> {
        val messages = remote.getTopic(streamId, topicName)
        return messageConverter.convert(messages)
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
}
