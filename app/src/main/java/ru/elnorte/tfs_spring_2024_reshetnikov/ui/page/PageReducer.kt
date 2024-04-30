package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviReducer
import javax.inject.Inject

class PageReducer @Inject constructor() : MviReducer<PagePartialState, PageState> {
    override fun reduce(
        prevState: PageState,
        partialState: PagePartialState,
    ): PageState {
        return when (partialState) {
            is PagePartialState.DataLoaded -> updatePageInState(partialState.model)
            PagePartialState.Loading -> PageLoading
        }
    }

    private fun updatePageInState(
        pageUi: List<PageItem>,
    ): PageState {
        return PageSuccess(pageUi)
    }
}
