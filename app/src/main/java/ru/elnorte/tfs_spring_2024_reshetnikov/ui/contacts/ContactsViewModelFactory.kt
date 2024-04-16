package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.IUserRepository

class ContactsViewModelFactory(private val repository: IUserRepository) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return ContactsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
