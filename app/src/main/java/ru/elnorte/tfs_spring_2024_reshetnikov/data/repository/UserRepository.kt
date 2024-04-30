package ru.elnorte.tfs_spring_2024_reshetnikov.data.repository

import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asPersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.remote.ProfileRemoteSource
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IUserRepository

@MainAnnotation.ProfileScope
class UserRepository(private val remote: ProfileRemoteSource) : IUserRepository {
    override suspend fun getPeople(): ResponseState<List<PersonUiModel>> {
        return remote.getPeople()
    }

    override suspend fun getMe(): ResponseState<PersonUiModel> {
        return when (val remoteData = remote.getMe()) {
            is ResponseState.Error -> remoteData
            is ResponseState.Success -> ResponseState.Success(remoteData.data.asPersonUiModel())
        }
    }

    override suspend fun queryContacts(queryText: String): ResponseState<List<PersonUiModel>> {
        return when (val contacts = getPeople()) {
            is ResponseState.Error -> contacts
            is ResponseState.Success -> {
                val filtered = contacts.data.filter {
                    it.name.contains(queryText, true)
                }
                ResponseState.Success(filtered)
            }
        }
    }
}
