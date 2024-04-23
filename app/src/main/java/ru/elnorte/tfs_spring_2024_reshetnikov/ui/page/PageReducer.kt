package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviReducer

class PageReducer : MviReducer<PagePartialState, PageState> {
    override fun reduce(
        prevState: PageState,
        partialState: PagePartialState,
    ): PageState {
        return when (partialState) {
            is PagePartialState.DataLoaded -> updatePageInState(prevState, partialState.model)
        }
    }

    private fun updatePageInState(
        prevState: PageState,
        pageUi: List<PageItem>,
    ): PageState {
        return prevState.copy(pageUi = PageUiState.Success(pageUi))
    }
}
