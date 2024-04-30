package ru.elnorte.tfs_spring_2024_reshetnikov.di.contacts

import dagger.Module
import dagger.Provides
import ru.elnorte.tfs_spring_2024_reshetnikov.data.convert.UserMapper
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApiService
import ru.elnorte.tfs_spring_2024_reshetnikov.data.remote.ProfileRemoteSourceImpl
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.UserRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile.ProfileActor
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile.ProfileEffect
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile.ProfileIntent
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile.ProfilePartialState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile.ProfileReducer
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile.ProfileState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile.ProfileStore
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IUserRepository

@Module
class ProfileModule {
    @Provides
    @MainAnnotation.ProfileScope
    fun provideRepository(api: MessengerApiService): IUserRepository {
        return UserRepository(ProfileRemoteSourceImpl(api, UserMapper()))
    }

    @Provides
    @MainAnnotation.ProfileScope
    fun provideProfileScopeStore(
        @MainAnnotation.ProfileScope repository: IUserRepository,
    ): MviStore<ProfilePartialState, ProfileIntent, ProfileState, ProfileEffect> {
        val reducer = ProfileReducer()
        val actor = ProfileActor(repository)
        return ProfileStore(reducer, actor)
    }
}
