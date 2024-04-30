package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviPartialState

sealed interface PagePartialState : MviPartialState {
    data object Loading : PagePartialState
    data class DataLoaded(val model: List<PageItem>) : PagePartialState
}
