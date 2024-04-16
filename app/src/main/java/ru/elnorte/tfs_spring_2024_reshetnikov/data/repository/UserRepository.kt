package ru.elnorte.tfs_spring_2024_reshetnikov.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApi
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.UsersMapper
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.asPersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel

class UserRepository : IUserRepository {
    override suspend fun getPeople(): List<PersonUiModel> {
        return withContext(Dispatchers.IO) {
            val data = MessengerApi.retrofitService.getUsers()
            val presence = MessengerApi.retrofitService.getUsersPresence()
            val output = UsersMapper(data, presence)
            output.sortedBy { it.isOnline }
        }
    }

    override suspend fun getMe(): PersonUiModel {
        val data = MessengerApi.retrofitService.getMineUser()
        return data.asPersonUiModel()
    }

    override suspend fun queryContacts(queryText: String): List<PersonUiModel> {
        return getPeople().filter {
            it.name.contains(queryText)
        }
    }

}
