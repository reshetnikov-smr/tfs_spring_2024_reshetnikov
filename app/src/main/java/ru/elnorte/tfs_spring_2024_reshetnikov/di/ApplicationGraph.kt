package ru.elnorte.tfs_spring_2024_reshetnikov.di

import dagger.Component
import ru.elnorte.tfs_spring_2024_reshetnikov.di.channels.ChannelComponent
import ru.elnorte.tfs_spring_2024_reshetnikov.di.chat.ChatComponent
import ru.elnorte.tfs_spring_2024_reshetnikov.di.contacts.ContactsComponent
import ru.elnorte.tfs_spring_2024_reshetnikov.di.contacts.ProfileComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationGraph {

    fun contactComponent(): ContactsComponent.Factory
    fun profileComponent(): ProfileComponent.Factory
    fun chatComponent(): ChatComponent.Factory
    fun channelComponent(): ChannelComponent.Factory
}
