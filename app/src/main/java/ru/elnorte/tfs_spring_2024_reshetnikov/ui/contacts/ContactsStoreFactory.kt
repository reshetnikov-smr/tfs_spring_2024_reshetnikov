package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContactsStoreFactory(
    private val reducer: ContactsReducer,
    private val actor: ContactsActor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactsStore(reducer, actor) as T
    }
}
