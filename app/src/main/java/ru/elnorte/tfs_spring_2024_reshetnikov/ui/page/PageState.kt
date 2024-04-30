package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviState

sealed class PageState : MviState

data object PageInit : PageState()

data class PageSuccess(val list: List<PageItem>) : PageState()

data object PageLoading : PageState()
