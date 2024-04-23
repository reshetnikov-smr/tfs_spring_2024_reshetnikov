package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviIntent

sealed interface ProfileIntent : MviIntent {
    data object Init : ProfileIntent
    data object ReloadPage : ProfileIntent
}
