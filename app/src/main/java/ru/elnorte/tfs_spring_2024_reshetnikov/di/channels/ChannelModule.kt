package ru.elnorte.tfs_spring_2024_reshetnikov.di.channels

import dagger.Module
import dagger.Provides
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.MessengerApiService
import ru.elnorte.tfs_spring_2024_reshetnikov.data.remote.ChannelRemoteSourceImpl
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.ChannelRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PageActor
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PageEffect
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PageIntent
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PagePartialState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PageReducer
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PageState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PageStore
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IChannelRepository

@Module
class ChannelModule {

    @Provides
    @MainAnnotation.ChannelScope
    fun provideRepository(api: MessengerApiService): IChannelRepository {
        return ChannelRepository(ChannelRemoteSourceImpl(api))
    }

    @Provides
    @MainAnnotation.ChannelScope
    fun providePageScopeStore(
        @MainAnnotation.ChannelScope repository: IChannelRepository,
    ): MviStore<PagePartialState, PageIntent, PageState, PageEffect> {
        val reducer = PageReducer()
        val actor = PageActor(repository)
        return PageStore(reducer, actor)
    }
}
