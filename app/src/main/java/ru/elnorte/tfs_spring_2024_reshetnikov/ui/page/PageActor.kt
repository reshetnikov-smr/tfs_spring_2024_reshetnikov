package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.ChannelRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicTransferModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.toPageItem
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviActor

class PageActor(private var repo: ChannelRepository) :
    MviActor<PagePartialState, PageIntent, PageState, PageEffect>() {

    private var channelsList = mutableListOf<PageItem>()
    override fun resolve(intent: PageIntent, state: PageState): Flow<PagePartialState> {
        return when (intent) {
            is PageIntent.ChannelItemClick -> channelItemClick(intent.channelId)
            is PageIntent.TopicItemClick -> topicItemClick(intent.topicName, intent.parent)
            is PageIntent.SendQuery -> sendQuery(intent.queryText, intent.isSubscribedOnly)
        }
    }

    private fun topicItemClick(topicName: String, parent: Int): Flow<PagePartialState> {
        return flow {
            _effects.emit(PageEffect.NavigateToChat(TopicTransferModel(topicName, parent)))
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
            runCatching {
                if (subscribedOnly) {
                    repo.querySubscribedChannels(queryText)

                } else {
                    repo.queryChannels(queryText)
                }
            }.fold(
                onSuccess = {
                    channelsList.clear()
                    channelsList.addAll(it.toPageItem())
                    emit(PagePartialState.DataLoaded(it.toPageItem()))
                },
                onFailure = {
                    _effects.emit(PageEffect.ShowError(it))
                }
            )
        }
    }
}
