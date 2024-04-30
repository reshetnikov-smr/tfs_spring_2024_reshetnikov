package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviReducer
import javax.inject.Inject

class ContactsReducer @Inject constructor() : MviReducer<ContactsPartialState, ContactsState> {
    override fun reduce(
        prevState: ContactsState,
        partialState: ContactsPartialState,
    ): ContactsState {
        return when (partialState) {
            is ContactsPartialState.DataLoaded -> updatePageInState(prevState, partialState.model)
            is ContactsPartialState.Update -> updatePageInState(prevState, partialState.contacts)
        }
    }

    private fun updatePageInState(
        prevState: ContactsState,
        personUi: List<PersonUiModel>,
    ): ContactsState {
        return prevState.copy(contactsUi = ContactsUiState.Success(personUi))
    }
}
