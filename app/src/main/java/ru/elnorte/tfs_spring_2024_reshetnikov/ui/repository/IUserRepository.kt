package ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState

interface IUserRepository {
    suspend fun getPeople(): ResponseState<List<PersonUiModel>>
    suspend fun getMe(): ResponseState<PersonUiModel>
    suspend fun queryContacts(queryText: String): ResponseState<List<PersonUiModel>>
}
