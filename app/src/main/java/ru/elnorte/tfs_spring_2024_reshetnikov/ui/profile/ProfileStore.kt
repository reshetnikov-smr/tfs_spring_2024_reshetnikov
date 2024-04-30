package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore
import javax.inject.Inject

@MainAnnotation.ProfileScope
class ProfileStore @Inject constructor(reducer: ProfileReducer, actor: ProfileActor) :
    MviStore<ProfilePartialState, ProfileIntent, ProfileState, ProfileEffect>(reducer, actor) {
    override fun initialStateCreator(): ProfileState = ProfileState(ProfileUiState.Init)
}
