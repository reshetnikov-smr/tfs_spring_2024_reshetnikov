package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore

class ProfileStore(reducer: ProfileReducer, actor: ProfileActor) :
    MviStore<ProfilePartialState, ProfileIntent, ProfileState, ProfileEffect>(reducer, actor) {
    override fun initialStateCreator(): ProfileState = ProfileState(ProfileUiState.Loading)
}
