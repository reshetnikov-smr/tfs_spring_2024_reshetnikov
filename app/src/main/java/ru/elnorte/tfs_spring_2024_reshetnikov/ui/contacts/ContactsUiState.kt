package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

sealed class ContactsUiState<out T : Any> {
    data class Success<out T : Any>(val data: T) : ContactsUiState<T>()
    data class Error(val errorMessage: String) : ContactsUiState<Nothing>()
    data object Loading : ContactsUiState<Nothing>()
}
