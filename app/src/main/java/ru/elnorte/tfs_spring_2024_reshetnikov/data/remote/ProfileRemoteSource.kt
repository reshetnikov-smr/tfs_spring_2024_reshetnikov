package ru.elnorte.tfs_spring_2024_reshetnikov.data.remote

import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MineUserResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState

interface ProfileRemoteSource {

    suspend fun getPeople(): ResponseState<List<PersonUiModel>>
    suspend fun getMe(): ResponseState<MineUserResponse>
}
