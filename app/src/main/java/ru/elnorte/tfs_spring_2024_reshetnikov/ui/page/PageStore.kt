package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore
import javax.inject.Inject

@MainAnnotation.ChannelScope
class PageStore @Inject constructor(reducer: PageReducer, actor: PageActor) :
    MviStore<PagePartialState, PageIntent, PageState, PageEffect>(reducer, actor) {
    override fun initialStateCreator(): PageState = PageInit
}
