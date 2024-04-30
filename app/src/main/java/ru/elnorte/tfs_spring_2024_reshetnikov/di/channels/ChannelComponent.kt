package ru.elnorte.tfs_spring_2024_reshetnikov.di.channels

import dagger.Subcomponent
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation.ChannelScope
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PageFragment

@ChannelScope
@Subcomponent(
    modules = [
        ChannelModule::class,
    ]
)
interface ChannelComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ChannelComponent
    }

    fun injectPageFragment(pageFragment: PageFragment)
}
