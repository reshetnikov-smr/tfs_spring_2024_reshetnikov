package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviPartialState

sealed interface ContactsPartialState : MviPartialState {
    data class DataLoaded(val model: List<PersonUiModel>) : ContactsPartialState
    class Update(val contacts: List<PersonUiModel>) : ContactsPartialState
}
