package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.IMessengerRepository

class PageViewModel(
    private val repository: IMessengerRepository,
    isSubscribedOnly: Boolean
) : ViewModel() {

    private var channelsList = mutableListOf<PageItem>()
    private val _model = MutableLiveData<List<PageItem>>()
    val model: LiveData<List<PageItem>>
        get() = _model

    private val _navigateToChat = MutableLiveData<Int?>()
    val navigateToChat: LiveData<Int?>
        get() = _navigateToChat

    init {
        channelsList = repository.getChannels(isSubscribedOnly).map { PageItem.ChannelItem(it) }
            .toMutableList()
        _model.value = channelsList.toList()
    }

    fun onChannelClick(channelId: Int) {
        val itemIndex = channelsList.indexOfFirst { it.id == channelId }
        val expandedState = (channelsList[itemIndex] as PageItem.ChannelItem).channel.isExpanded
        val item =
            (channelsList[itemIndex] as PageItem.ChannelItem).channel.copy(isExpanded = !expandedState)
        channelsList.removeAt(itemIndex)
        channelsList.add(itemIndex, PageItem.ChannelItem(item))
        val channelContent = repository.getChannelContent(channelId)

        if (!expandedState) {
            channelsList.addAll(itemIndex + 1, channelContent.map { PageItem.TopicItem(it) })
        } else {
            channelsList.removeAll(channelContent.map { PageItem.TopicItem(it) })
        }
        _model.value = channelsList.toList()
    }

    fun onTopicClick(topicId: Int) {
        _navigateToChat.value = topicId
    }

    fun navigateToChatCompleted() {
        _navigateToChat.value = null
    }
}
