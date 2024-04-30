package ru.elnorte.tfs_spring_2024_reshetnikov.di.chat

import dagger.Subcomponent
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic.TopicFragment

@MainAnnotation.ChatScope
@Subcomponent(
    modules = [
        ChatModule::class,
    ]
)
interface ChatComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ChatComponent
    }

    @MainAnnotation.ChatScope
    fun inject(topicFragment: TopicFragment)
}
