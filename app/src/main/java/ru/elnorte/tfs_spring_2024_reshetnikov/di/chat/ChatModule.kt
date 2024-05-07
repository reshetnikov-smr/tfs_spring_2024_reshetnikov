package ru.elnorte.tfs_spring_2024_reshetnikov.di.chat

import ChatRepository
import dagger.Module
import dagger.Provides
import ru.elnorte.tfs_spring_2024_reshetnikov.data.convert.MessageConverter
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.ChatLocalSourceImpl
import ru.elnorte.tfs_spring_2024_reshetnikov.data.local.dao.ChatDao
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApiService
import ru.elnorte.tfs_spring_2024_reshetnikov.data.remote.ChatRemoteSourceImpl
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IChatRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic.TopicActor
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic.TopicEffect
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic.TopicIntent
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic.TopicPartialState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic.TopicReducer
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic.TopicState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic.TopicStore

@Module
class ChatModule {
    @Provides
    @MainAnnotation.ChatScope
    fun provideRepository(api: MessengerApiService, dao: ChatDao): IChatRepository {
        return ChatRepository(
            MessageConverter(),
            ChatRemoteSourceImpl(api),
            ChatLocalSourceImpl(dao)
        )
    }

    @Provides
    @MainAnnotation.ChatScope
    fun provideChatScopeStore(
        @MainAnnotation.ChatScope repository: IChatRepository,
    ): MviStore<TopicPartialState, TopicIntent, TopicState, TopicEffect> {
        val reducer = TopicReducer()
        val actor = TopicActor(repository)
        return TopicStore(reducer, actor)
    }
}
