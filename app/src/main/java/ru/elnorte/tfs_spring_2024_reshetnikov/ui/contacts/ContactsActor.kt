package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviActor
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IUserRepository
import javax.inject.Inject

class ContactsActor @Inject constructor(private var repo: IUserRepository) :
    MviActor<ContactsPartialState, ContactsIntent, ContactsState, ContactsEffect>() {

    override fun resolve(intent: ContactsIntent, state: ContactsState): Flow<ContactsPartialState> {
        return when (intent) {
            ContactsIntent.Init -> loadData()
            ContactsIntent.Update -> loadData()
            is ContactsIntent.FilterQuery -> queryFilter(intent.queryText)
        }
    }

    private fun queryFilter(queryText: String): Flow<ContactsPartialState> {
        return flow {
            when (val contact = repo.queryContacts(queryText)) {
                is ResponseState.Error -> {
                    emit(ContactsPartialState.Update(emptyList()))
                    _effects.emit(ContactsEffect.ShowError(contact.errorMessage))
                }

                is ResponseState.Success -> {
                    emit(ContactsPartialState.Update(contact.data))
                }
            }
        }
    }

    private fun loadData(): Flow<ContactsPartialState> {
        return flow {
            when (val contact = repo.getPeople()) {
                is ResponseState.Error -> {
                    emit(ContactsPartialState.Update(emptyList()))
                    _effects.emit(ContactsEffect.ShowError(contact.errorMessage))
                }

                is ResponseState.Success -> emit(ContactsPartialState.DataLoaded(contact.data))
            }
        }
    }
}
