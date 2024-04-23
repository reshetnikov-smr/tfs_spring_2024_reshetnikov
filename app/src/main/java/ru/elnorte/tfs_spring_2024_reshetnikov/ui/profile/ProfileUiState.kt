package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile


sealed class ProfileUiState<out T : Any> {
    data class Success<out T : Any>(val data: T) : ProfileUiState<T>()
    data class Error(val errorMessage: String) : ProfileUiState<Nothing>()
    data object Loading : ProfileUiState<Nothing>()
}
