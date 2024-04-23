package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviState

data class ProfileState(val profileUi: ProfileUiState<PersonUiModel>) : MviState
