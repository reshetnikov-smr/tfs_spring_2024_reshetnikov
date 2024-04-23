package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.UserRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviActor

class ProfileActor(private var repo: UserRepository) :
    MviActor<ProfilePartialState, ProfileIntent, ProfileState, ProfileEffect>() {

    override fun resolve(intent: ProfileIntent, state: ProfileState): Flow<ProfilePartialState> {
        return when (intent) {
            ProfileIntent.Init -> loadData()
            ProfileIntent.ReloadPage -> loadData()
        }
    }

    private fun loadData(): Flow<ProfilePartialState> {
        return flow {
            runCatching {
                repo.getMe()
            }.fold(
                onSuccess = {
                    emit(ProfilePartialState.DataLoaded(it))
                },
                onFailure = {
                    _effects.emit(ProfileEffect.ShowError(it))
                }
            )
        }
    }
}
