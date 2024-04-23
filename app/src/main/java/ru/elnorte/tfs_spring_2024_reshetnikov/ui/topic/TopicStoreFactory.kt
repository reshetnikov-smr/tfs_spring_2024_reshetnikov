package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TopicStoreFactory(
    private val reducer: TopicReducer,
    private val actor: TopicActor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TopicStore(reducer, actor) as T
    }
}
