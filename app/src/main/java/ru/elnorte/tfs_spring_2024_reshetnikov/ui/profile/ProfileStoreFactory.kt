package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileStoreFactory(
    private val reducer: ProfileReducer,
    private val actor: ProfileActor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileStore(reducer, actor) as T
    }
}
