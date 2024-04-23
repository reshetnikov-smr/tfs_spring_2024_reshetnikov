package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

sealed class TopicUiState<out T : Any> {
    data class Success<out T : Any>(val data: T) : TopicUiState<T>()
    data class Error(val errorMessage: String) : TopicUiState<Nothing>()
    data object Loading : TopicUiState<Nothing>()
}
