package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResponseState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviActor
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IUserRepository
import javax.inject.Inject

@MainAnnotation.ProfileScope
class ProfileActor @Inject constructor(private var repo: IUserRepository) :
    MviActor<ProfilePartialState, ProfileIntent, ProfileState, ProfileEffect>() {

    override fun resolve(intent: ProfileIntent, state: ProfileState): Flow<ProfilePartialState> {
        return when (intent) {
            ProfileIntent.Init -> loadData()
        }
    }

    private fun loadData(): Flow<ProfilePartialState> {
        return flow {
            emit(ProfilePartialState.Loading)
            when (val meData = repo.getMe()) {
                is ResponseState.Error -> {
                    emit(ProfilePartialState.Error)
                    _effects.emit(ProfileEffect.ShowError(meData.errorMessage))
                }

                is ResponseState.Success -> emit(ProfilePartialState.DataLoaded(meData.data))
            }
        }
    }
}
