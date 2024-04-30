package ru.elnorte.tfs_spring_2024_reshetnikov.di.contacts

import dagger.Module
import dagger.Provides
import ru.elnorte.tfs_spring_2024_reshetnikov.data.convert.UserMapper
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApiService
import ru.elnorte.tfs_spring_2024_reshetnikov.data.remote.ProfileRemoteSourceImpl
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.UserRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts.ContactsActor
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts.ContactsEffect
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts.ContactsIntent
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts.ContactsPartialState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts.ContactsReducer
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts.ContactsState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts.ContactsStore
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IUserRepository

@Module
class ContactModule {

    @Provides
    @MainAnnotation.ContactScope
    fun provideRepository(api: MessengerApiService): IUserRepository {
        return UserRepository(ProfileRemoteSourceImpl(api, UserMapper()))
    }

    @Provides
    @MainAnnotation.ContactScope
    fun provideContactScopeStore(
        repository: IUserRepository,
    ): MviStore<ContactsPartialState, ContactsIntent, ContactsState, ContactsEffect> {
        val reducer = ContactsReducer()
        val actor = ContactsActor(repository)
        return ContactsStore(reducer, actor)
    }
}
