package ru.elnorte.tfs_spring_2024_reshetnikov.di.contacts

import dagger.Subcomponent
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts.ContactsFragment

@MainAnnotation.ContactScope
@Subcomponent(
    modules = [
        ContactModule::class,
    ]
)
interface ContactsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ContactsComponent
    }

    @MainAnnotation.ContactScope
    fun inject(contactsFragment: ContactsFragment)
}
