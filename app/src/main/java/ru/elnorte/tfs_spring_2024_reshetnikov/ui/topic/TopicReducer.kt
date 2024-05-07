package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChatUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviReducer
import javax.inject.Inject

class TopicReducer @Inject constructor() : MviReducer<TopicPartialState, TopicState> {
    override fun reduce(prevState: TopicState, partialState: TopicPartialState): TopicState {
        return when (partialState) {
            is TopicPartialState.InitLoaded -> updatePageInState(partialState.model)

            is TopicPartialState.ActionButtonChanged -> updateActionButtonInState(
                prevState,
                partialState.isSend
            )

            TopicPartialState.NoChanges -> prevState
            is TopicPartialState.MessagesLoaded -> messagesLoaded(prevState, partialState.model)
            is TopicPartialState.MessagesSent -> messageSent(prevState, partialState.messages)
            TopicPartialState.Error -> errorHappened(prevState)
        }
    }

    private fun errorHappened(prevState: TopicState): TopicState {
        return TopicError

    }

    private fun messageSent(prevState: TopicState, messages: List<MessageUiModel>): TopicState {
        return if (prevState is TopicSuccess) {
            val data = prevState.data.copy(messages = messages)
            TopicSuccess(data, showLastMessage = true)
        } else {
            prevState
        }
    }

    private fun messagesLoaded(prevState: TopicState, model: List<MessageUiModel>): TopicState =
        if (prevState is TopicSuccess) {
            val data = prevState.data.copy(messages = model)
            TopicSuccess(data)
        } else {
            prevState
        }

    private fun updatePageInState(
        current: ChatUiModel,
    ): TopicState {
        return TopicSuccess(current, true)
    }

    private fun updateActionButtonInState(prevState: TopicState, isSend: Boolean): TopicState {
        if (prevState is TopicSuccess) {
            return if (isSend) {
                TopicSuccess(
                    prevState.data.copy(messageState = MessageState.SEND_FILE),
                    showLastMessage = prevState.showLastMessage
                )
            } else {
                TopicSuccess(
                    prevState.data.copy(messageState = MessageState.SEND_MESSAGE),
                    showLastMessage = prevState.showLastMessage
                )
            }
        }
        return prevState
    }
}
