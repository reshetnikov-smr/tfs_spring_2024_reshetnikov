package ru.elnorte.tfs_spring_2024_reshetnikov.data.repository

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel

interface IUserRepository {
    suspend fun getPeople(): List<PersonUiModel>

    suspend fun getMe(): PersonUiModel

    suspend fun queryContacts(queryText: String): List<PersonUiModel>
}
