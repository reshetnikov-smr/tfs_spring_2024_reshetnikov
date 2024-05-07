package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.toPageItem
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviActor
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.repository.IChannelRepository
import javax.inject.Inject

class PageActor @Inject constructor(private var repo: IChannelRepository) :
    MviActor<PagePartialState, PageIntent, PageState, PageEffect>() {

    private var channelsList = mutableListOf<PageItem>()
    private var currentQueryText: String = ""

    override fun resolve(intent: PageIntent, state: PageState): Flow<PagePartialState> {
        return when (intent) {
            is PageIntent.ChannelItemClick -> channelItemClick(intent.channelId)
            is PageIntent.SendQuery -> sendQuery(intent.queryText, intent.isSubscribedOnly)
            is PageIntent.Init -> initStreams(intent.isSubscribedOnly)
        }
    }


    private fun initStreams(subscribedOnly: Boolean): Flow<PagePartialState> {
        currentQueryText = ""
        return flow {
            getStreams(subscribedOnly, currentQueryText, true)
        }
    }

    private suspend fun FlowCollector<PagePartialState>.getStreams(
        subscribedOnly: Boolean,
        queryText: String,
        isFirst: Boolean = false,
    ) {
        if (subscribedOnly) {
            repo.querySubscribedChannels(queryText, isFirst).collect {
                updateInitList(it)
                emit(PagePartialState.DataLoaded(it.toPageItem()))
            }
        } else {
            repo.queryChannels(queryText, isFirst).collect {
                updateInitList(it)
                emit(PagePartialState.DataLoaded(it.toPageItem()))
            }
        }
    }

    private fun channelItemClick(channelId: Int): Flow<PagePartialState> {
        return flow {
            runCatching {
                val itemIndex = channelsList.indexOfFirst { it.id == channelId }
                val expandedState =
                    (channelsList[itemIndex] as PageItem.ChannelItem).channel.isExpanded
                val item =
                    (channelsList[itemIndex] as PageItem.ChannelItem).channel.copy(isExpanded = !expandedState)
                channelsList.removeAt(itemIndex)
                channelsList.add(itemIndex, PageItem.ChannelItem(item))

                val channelContent = repo.getTopics(channelId)

                if (!expandedState) {
                    channelsList.addAll(
                        itemIndex + 1,
                        channelContent.map { PageItem.TopicItem(it) })
                } else {
                    channelsList.removeAll(channelContent.map { PageItem.TopicItem(it) })
                }
                channelsList.toList()

            }.fold(
                onSuccess = { emit(PagePartialState.DataLoaded(it)) },
                onFailure = { _effects.emit(PageEffect.ShowError(it)) },
            )
        }
    }

    private fun sendQuery(queryText: String, subscribedOnly: Boolean): Flow<PagePartialState> {
        return flow {
            if (queryText != currentQueryText) {
                currentQueryText = queryText
                getStreams(subscribedOnly, currentQueryText, false)
            }
        }
    }

    /*    private suspend fun FlowCollector<PagePartialState>.handleQuery(
            subscribedOnly: Boolean,
            queryText: String,
        ) {
            runCatching {
                if (subscribedOnly) {
                    repo.querySubscribedChannels(queryText)
                } else {
                    repo.queryChannels(queryText)
                }
            }.fold(
                onSuccess = {
                    updateInitList(it)
                    emit(PagePartialState.DataLoaded(it.toPageItem()))
                },
                onFailure = {
                    emit(PagePartialState.DataLoaded(emptyList()))
                    _effects.emit(PageEffect.ShowError(it))
                }
            )
        }*/

    private fun updateInitList(it: List<ChannelUiModel>) {
        channelsList.clear()
        channelsList.addAll(it.toPageItem())
    }
}
