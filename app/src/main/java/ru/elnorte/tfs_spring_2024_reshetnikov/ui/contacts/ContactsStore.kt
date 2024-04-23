package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore

class ContactsStore(reducer: ContactsReducer, actor: ContactsActor) :
    MviStore<ContactsPartialState, ContactsIntent, ContactsState, ContactsEffect>(reducer, actor) {
    override fun initialStateCreator(): ContactsState = ContactsState(ContactsUiState.Loading)
    private var queryText: String = ""
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            fetchContactOnline(10000)
        }
    }

    private suspend fun fetchContactOnline(period: Long) {
        while (true) {
            super.postIntent(ContactsIntent.FilterQuery(queryText))
            delay(period)
        }
    }

    fun updateQuery(query: String) {
        queryText = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(2000)
            if (!isActive) return@launch
            postIntent(ContactsIntent.FilterQuery(query))
        }
    }
}
