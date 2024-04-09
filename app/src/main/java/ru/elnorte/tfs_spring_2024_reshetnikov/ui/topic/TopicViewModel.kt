package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.IMessengerRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel

class TopicViewModel(var topicId: Int, private var repository: IMessengerRepository) : ViewModel() {

    private val _messageState = MutableLiveData<MessageState>()
    val messageState: LiveData<MessageState>
        get() = _messageState

    private val _messagesList = MutableLiveData<List<MessageUiModel>>()
    val messagesList: LiveData<List<MessageUiModel>>
        get() = _messagesList
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

    fun toggleReaction(messageId: Int, emoji: String) {
        viewModelScope.launch { repository.toggleReaction(topicId, messageId, emoji) }
        fetchData()
    }

    fun onErrorCompleted() {
        _error.value = null
    }

    private fun sendFile() {}

    private fun sendMessage(text: String) {
        viewModelScope.launch {
            if ((0..1).random() == 1) {
                repository.addMessage(topicId, text)
                fetchData()
            } else {
                _error.postValue("Не получилось отправить")
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch { _messagesList.value = repository.getTopic(topicId) }
    }
}

enum class MessageState {
    SEND_FILE, SEND_MESSAGE
}
