package ru.elnorte.tfs_spring_2024_reshetnikov.ui.models

sealed class ResultUiState<out T : Any> {
    data class Success<out T : Any>(val dataList: List<T>) : ResultUiState<T>()
    data class Error(val errorMessage: String) : ResultUiState<Nothing>()
    data object Loading : ResultUiState<Nothing>()
}
