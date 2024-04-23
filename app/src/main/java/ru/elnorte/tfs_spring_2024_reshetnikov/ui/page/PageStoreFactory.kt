package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PageStoreFactory(
    private val reducer: PageReducer,
    private val actor: PageActor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PageStore(reducer, actor) as T
    }
}
