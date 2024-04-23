package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviEffect

sealed interface ProfileEffect : MviEffect {
    data class ShowError(val throwable: Throwable) : ProfileEffect

}
