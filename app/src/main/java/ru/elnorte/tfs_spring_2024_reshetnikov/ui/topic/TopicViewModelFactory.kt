package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.IMessengerRepository

class TopicViewModelFactory(var topicId: Int, private val repository: IMessengerRepository) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopicViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return TopicViewModel(topicId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
