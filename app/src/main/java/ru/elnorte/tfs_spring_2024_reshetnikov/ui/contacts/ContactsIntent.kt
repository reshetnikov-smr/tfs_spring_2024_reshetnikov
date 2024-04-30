package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviIntent

sealed interface ContactsIntent : MviIntent {
    data object Init : ContactsIntent
    data object Update : ContactsIntent
    data class FilterQuery(val queryText: String) : ContactsIntent
}
