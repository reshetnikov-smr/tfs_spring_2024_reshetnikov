package ru.elnorte.tfs_spring_2024_reshetnikov.di.contacts

import dagger.Subcomponent
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile.ProfileFragment

@MainAnnotation.ProfileScope
@Subcomponent(
    modules = [
        ProfileModule::class,
    ]
)
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileComponent
    }

    @MainAnnotation.ProfileScope
    fun inject(profileFragment: ProfileFragment)
}
