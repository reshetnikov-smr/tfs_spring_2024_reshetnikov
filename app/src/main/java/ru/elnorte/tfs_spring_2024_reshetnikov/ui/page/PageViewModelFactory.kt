package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.IMessengerRepository

class PageViewModelFactory(
    private val repository: IMessengerRepository,
    private val isSubscribedOnly: Boolean
) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return PageViewModel(repository, isSubscribedOnly) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
