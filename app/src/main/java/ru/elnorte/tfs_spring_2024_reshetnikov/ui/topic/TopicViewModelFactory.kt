package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.IChannelRepository

class TopicViewModelFactory(
    private val channelId: Int,
    private val topicName: String,
    private val repository: IChannelRepository
) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopicViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return TopicViewModel(channelId, topicName, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
