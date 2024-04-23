package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviState

data class PageState(val pageUi: PageUiState<List<PageItem>>) : MviState
