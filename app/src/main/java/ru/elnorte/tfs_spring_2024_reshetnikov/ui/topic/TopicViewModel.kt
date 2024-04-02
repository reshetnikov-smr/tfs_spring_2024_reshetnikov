package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.IMessengerRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel

class TopicViewModel(var topicId: Int, private var repository: IMessengerRepository) : ViewModel() {

    private val _messageState = MutableLiveData<MessageState>()
    val messageState: LiveData<MessageState>
        get() = _messageState

    private val _messagesList = MutableLiveData<List<MessageUiModel>>()
    val messagesList: LiveData<List<MessageUiModel>>
        get() = _messagesList

    init {
        _messageState.value = MessageState.SEND_FILE
        fetchData()
    }

    private fun fetchData() {
        _messagesList.value = repository.getTopic(topicId)
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
        repository.toggleReaction(topicId, messageId, emoji)
        fetchData()
    }

    private fun sendFile() {}
    private fun sendMessage(text: String) {
        repository.addMessage(topicId, text)
        fetchData()
    }
}

enum class MessageState {
    SEND_FILE, SEND_MESSAGE
}
