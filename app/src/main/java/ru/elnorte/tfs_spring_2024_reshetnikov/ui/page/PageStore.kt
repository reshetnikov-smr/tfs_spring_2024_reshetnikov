package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore

class PageStore(reducer: PageReducer, actor: PageActor) :
    MviStore<PagePartialState, PageIntent, PageState, PageEffect>(reducer, actor) {
    override fun initialStateCreator(): PageState = PageState(PageUiState.Loading)
}
