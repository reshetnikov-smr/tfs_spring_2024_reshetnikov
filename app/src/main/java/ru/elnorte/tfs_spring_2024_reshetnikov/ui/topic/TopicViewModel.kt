package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.IChannelRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState

class TopicViewModel(
    private val channelId: Int,
    private val topicName: String,
    private val repository: IChannelRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ResultUiState<MessageUiModel>> =
        MutableStateFlow(ResultUiState.Loading)

    val uiState: StateFlow<ResultUiState<MessageUiModel>> = _uiState

    private val _messageState = MutableLiveData<MessageState>()
    val messageState: LiveData<MessageState>
        get() = _messageState

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    init {
        _messageState.value = MessageState.SEND_FILE
        fetchData()
    }

    fun onActionButtonClick(text: String) {
        when (messageState.value) {
            MessageState.SEND_FILE -> sendFile()
            MessageState.SEND_MESSAGE -> sendMessage(text)
            else -> Unit
        }
    }

    fun onTextChanged(s: String) {
        if (s.isNotEmpty()) {
            _messageState.value = MessageState.SEND_MESSAGE
        } else {
            _messageState.value = MessageState.SEND_FILE
        }
    }

    fun addReaction(messageId: Int, emoji: String) {
        viewModelScope.launch {
            repository.addReaction(messageId, emoji)?.let {
                _uiState.value = it
            }
        }
    }

    fun removeReaction(messageId: Int, emoji: String) {
        viewModelScope.launch {
            repository.removeReaction(messageId, emoji)
        }
    }

    fun onErrorCompleted() {
        _error.value = null
    }

    private fun sendFile() {}

    private fun sendMessage(text: String) {
        viewModelScope.launch {
            repository.sendMessage(channelId, topicName, text)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            repository.getTopic(channelId, topicName)
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = it
                }
        }
    }
}


enum class MessageState {
    SEND_FILE, SEND_MESSAGE
}
