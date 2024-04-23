package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

sealed class PageUiState<out T : Any> {
    data class Success<out T : Any>(val data: T) : PageUiState<T>()
    data class Error(val errorMessage: String) : PageUiState<Nothing>()
    data object Loading : PageUiState<Nothing>()
}
