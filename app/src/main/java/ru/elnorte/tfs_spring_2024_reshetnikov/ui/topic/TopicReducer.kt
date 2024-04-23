package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import ru.elnorte.tfs_spring_2024_reshetnikov.ellog
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChatUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviReducer

class TopicReducer : MviReducer<TopicPartialState, TopicState> {
    override fun reduce(prevState: TopicState, partialState: TopicPartialState): TopicState {
        ellog("reduce update ${partialState.javaClass}")
        return when (partialState) {
            is TopicPartialState.DataLoaded -> updatePageInState(
                prevState, partialState.model
            )

            is TopicPartialState.ActionButtonChanged -> updateActionButtonInState(
                prevState,
                partialState.isSend
            )

            TopicPartialState.NoChanges -> prevState
            is TopicPartialState.MessagesLoaded -> messagesLoaded(prevState, partialState.model)
        }
    }

    private fun messagesLoaded(prevState: TopicState, model: List<MessageUiModel>): TopicState {
        val prevUIState = prevState.topicUi
        if (prevUIState is TopicUiState.Success) {
            val oldState = prevUIState.data
            return TopicState(TopicUiState.Success(oldState.copy(messages = model)))
        }
        return prevState
    }

    private fun updatePageInState(
        prevState: TopicState,
        current: ChatUiModel,
    ): TopicState {
        return TopicState(TopicUiState.Success(current))
    }

    private fun updateActionButtonInState(prevState: TopicState, isSend: Boolean): TopicState {
        val prevUIState = prevState.topicUi
        if (prevUIState is TopicUiState.Success) {
            val oldState = prevUIState.data
            return if (isSend) {
                TopicState(TopicUiState.Success(oldState.copy(messageState = MessageState.SEND_FILE)))
            } else {
                TopicState(TopicUiState.Success(oldState.copy(messageState = MessageState.SEND_MESSAGE)))
            }
        }
        return prevState
    }
}
