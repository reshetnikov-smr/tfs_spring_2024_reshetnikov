package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.ChannelRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ellog
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChatUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviActor

class TopicActor(private var repo: ChannelRepository) :
    MviActor<TopicPartialState, TopicIntent, TopicState, TopicEffect>() {
    private var channelId: Int = 0
    private var topicName: String = ""
    override fun resolve(intent: TopicIntent, state: TopicState): Flow<TopicPartialState> {
        return when (intent) {
            is TopicIntent.ActionButtonClickIntent -> onActionButtonClick(intent.message, state)
            is TopicIntent.AddReactionClick -> openDialogEffect(intent.messageId)
            is TopicIntent.Init -> loadData(intent.channelId, intent.topicName)
            is TopicIntent.LongMessageClick -> callDialogEffect(intent.messageId)
            TopicIntent.ReloadPage -> fetchData()
            is TopicIntent.AddReaction -> addReaction(intent.messageId, intent.emoji)
            is TopicIntent.RemoveReaction -> removeReaction(intent.messageId, intent.emoji)
            is TopicIntent.TextEnteredIntent -> changeActionButton(intent.isEmpty)
            TopicIntent.NavigateBack -> navigateBack()
        }
    }

    private fun navigateBack(): Flow<TopicPartialState> {
        return flow {
            _effects.emit(TopicEffect.NavigateBack)
        }
    }

    private fun onActionButtonClick(message: String, state: TopicState): Flow<TopicPartialState> {
        return if ((state.topicUi as TopicUiState.Success).data.messageState == MessageState.SEND_MESSAGE) {
            sendMessage(message)
        } else {
            flow {
                emit(TopicPartialState.NoChanges)
            }
        }
    }

    private fun loadData(channelId: Int, topicName: String): Flow<TopicPartialState> {
        this.channelId = channelId
        this.topicName = topicName
        return flow {
            //todo repository must return state already
            runCatching {
                ellog("runCatching")
                repo.getTopic(channelId, topicName)
            }.fold(
                onSuccess = {
                    emit(TopicPartialState.DataLoaded(ChatUiModel(channelId, topicName, it)))
                },
                onFailure = {
                    ellog("onFailure")
                    _effects.emit(TopicEffect.ShowError(it))
                }
            )
        }
    }

    private fun fetchData(): Flow<TopicPartialState> {
        return flow {
            //todo repository must return state already
            runCatching {
                repo.getTopic(channelId, topicName)
            }.fold(
                onSuccess = {
                    emit(TopicPartialState.MessagesLoaded(it))
                },
                onFailure = {
                    _effects.emit(TopicEffect.ShowError(it))
                }
            )
        }
    }

    private fun changeActionButton(isEmpty: Boolean): Flow<TopicPartialState> {
        return flow {
            emit(TopicPartialState.ActionButtonChanged(isEmpty))
        }
    }

    private fun callDialogEffect(messageId: Int): Flow<TopicPartialState> {
        return flow {
            _effects.emit(TopicEffect.ShowEmojiDialog(messageId))
        }
    }

    private fun openDialogEffect(messageId: Int): Flow<TopicPartialState> {
        return flow {
            _effects.emit(TopicEffect.ShowEmojiDialog(messageId))
        }
    }

    private fun addReaction(messageId: Int, emoji: String): Flow<TopicPartialState> {
        return flow {
            runCatching {
                repo.addReaction(messageId, emoji)
            }.fold(
                onSuccess = {},
                onFailure = { _effects.emit(TopicEffect.ShowError(it)) },
            )
        }
    }

    private fun removeReaction(messageId: Int, emoji: String): Flow<TopicPartialState> {
        return flow {
            runCatching {
                repo.removeReaction(messageId, emoji)
            }.fold(
                onSuccess = {},
                onFailure = { _effects.emit(TopicEffect.ShowError(it)) },
            )
        }
    }

    private fun sendMessage(text: String): Flow<TopicPartialState> {
        return flow {
            runCatching {
                repo.sendMessage(channelId, topicName, text)
            }.fold(
                onSuccess = {},
                onFailure = { _effects.emit(TopicEffect.ShowError(it)) },
            )
        }
    }
}

enum class MessageState {
    SEND_FILE, SEND_MESSAGE
}
