package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviPartialState

sealed interface ProfilePartialState : MviPartialState {
    data class DataLoaded(val model: PersonUiModel) : ProfilePartialState
}
