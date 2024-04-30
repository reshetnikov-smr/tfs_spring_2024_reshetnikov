package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviReducer
import javax.inject.Inject

class ProfileReducer @Inject constructor() : MviReducer<ProfilePartialState, ProfileState> {
    override fun reduce(prevState: ProfileState, partialState: ProfilePartialState): ProfileState {
        return when (partialState) {
            is ProfilePartialState.DataLoaded -> updatePageInState(
                prevState, partialState.model
            )

            ProfilePartialState.Error -> ProfileState(ProfileUiState.Error(""))
            ProfilePartialState.Loading -> ProfileState(ProfileUiState.Loading)
        }
    }

    private fun updatePageInState(
        prevState: ProfileState,
        personUi: PersonUiModel,
    ): ProfileState {
        return prevState.copy(profileUi = ProfileUiState.Success(personUi))
    }
}
