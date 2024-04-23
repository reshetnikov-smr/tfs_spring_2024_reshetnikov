package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.UserRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.OnlineStatus
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviActor

class ContactsActor(private var repo: UserRepository) :
    MviActor<ContactsPartialState, ContactsIntent, ContactsState, ContactsEffect>() {

    override fun resolve(intent: ContactsIntent, state: ContactsState): Flow<ContactsPartialState> {
        return when (intent) {
            ContactsIntent.Init -> loadData()
            ContactsIntent.Update -> loadData()
            is ContactsIntent.FilterQuery -> queryFilter(intent.queryText)
            is ContactsIntent.NavigateToPerson -> navigateToPerson(
                intent.avatar,
                intent.name,
                intent.status,
                intent.isOnline
            )
        }
    }


    private fun queryFilter(queryText: String): Flow<ContactsPartialState> {
        return flow {
            runCatching {
                repo.queryContacts(queryText)
            }.fold(
                onSuccess = {
                    emit(ContactsPartialState.Update(it))
                },
                onFailure = {
                    _effects.emit(ContactsEffect.ShowError(it))
                }
            )
        }
    }

    private fun navigateToPerson(
        avatar: String?,
        name: String,
        status: String,
        online: OnlineStatus,
    ): Flow<ContactsPartialState> {
        return flow {
            _effects.emit(ContactsEffect.NavigateToPerson(avatar, name, status, online))
        }
    }

    private fun loadData(): Flow<ContactsPartialState> {
        return flow {
            //todo repository must return state already
            runCatching {
                repo.getPeople()
            }.fold(
                onSuccess = {
                    emit(ContactsPartialState.DataLoaded(it))
                },
                onFailure = {
                    _effects.emit(ContactsEffect.ShowError(it))
                }
            )
        }
    }
}
