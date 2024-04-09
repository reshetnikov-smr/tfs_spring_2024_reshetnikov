package ru.elnorte.tfs_spring_2024_reshetnikov.ui.models

sealed class QueryResultUiState<out T : Any> {
    data class Success<out T : Any>(val dataList: List<T>) : QueryResultUiState<T>()
    data class Error(val exception: Throwable) : QueryResultUiState<Nothing>()
    data object Loading : QueryResultUiState<Nothing>()
}
