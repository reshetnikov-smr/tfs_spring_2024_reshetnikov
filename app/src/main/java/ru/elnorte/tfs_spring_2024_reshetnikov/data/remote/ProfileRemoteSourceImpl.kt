package ru.elnorte.tfs_spring_2024_reshetnikov.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.elnorte.tfs_spring_2024_reshetnikov.data.convert.UserMapper
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApiService
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.MineUserResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState

@MainAnnotation.ProfileScope
class ProfileRemoteSourceImpl(
    private val api: MessengerApiService,
    private val mapper: UserMapper,
) : ProfileRemoteSource {

    override suspend fun getPeople(): ResponseState<List<PersonUiModel>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val data = api.getUsers().execute().body()
                val presence = api.getUsersPresence().execute().body()
                checkNotNull(data) { "empty getUser" }
                checkNotNull(presence) { "empty getUsersPresence" }
                mapper.convert(data, presence)
            }.fold(
                onSuccess = { data ->
                    val sorted = data.sortedBy { it.isOnline }
                    ResponseState.Success(sorted)
                },
                onFailure = { exception -> ResponseState.Error(exception.message.orEmpty()) }
            )
        }
    }

    override suspend fun getMe(): ResponseState<MineUserResponse> {
        return withContext(Dispatchers.IO) {
            runCatching {
                checkNotNull(
                    api.getMineUser().execute().body()
                ) { "empty data" }
            }.fold(
                onSuccess = { data ->
                    ResponseState.Success(data)
                },
                onFailure = { ResponseState.Error(it.message.orEmpty()) }
            )
        }
    }
}
