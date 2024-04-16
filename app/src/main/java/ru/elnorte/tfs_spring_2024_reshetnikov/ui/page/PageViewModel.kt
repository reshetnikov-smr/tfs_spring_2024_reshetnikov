package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.IChannelRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicTransferModel

class PageViewModel(
    private val repository: IChannelRepository,
    private val isSubscribedOnly: Boolean,
) : ViewModel() {

    private var channelsList = mutableListOf<PageItem>()

    private val _navigateToChat = MutableLiveData<TopicTransferModel>()
    val navigateToChat: LiveData<TopicTransferModel>
        get() = _navigateToChat

    private val _queryFlow = MutableStateFlow("")

    private val _uiState: MutableStateFlow<ResultUiState<PageItem>> =
        MutableStateFlow(ResultUiState.Loading)

    val uiState: StateFlow<ResultUiState<PageItem>> = _uiState

    init {
        viewModelScope.launch {
            query()
        }
    }

    fun onChannelClick(channelId: Int) {
        viewModelScope.launch {
            val itemIndex = channelsList.indexOfFirst { it.id == channelId }
            val expandedState = (channelsList[itemIndex] as PageItem.ChannelItem).channel.isExpanded
            val item =
                (channelsList[itemIndex] as PageItem.ChannelItem).channel.copy(isExpanded = !expandedState)
            channelsList.removeAt(itemIndex)
            channelsList.add(itemIndex, PageItem.ChannelItem(item))

            val channelContent = repository.getTopics(channelId)

            if (!expandedState) {
                channelsList.addAll(itemIndex + 1, channelContent.map { PageItem.TopicItem(it) })
            } else {
                channelsList.removeAll(channelContent.map { PageItem.TopicItem(it) })
            }
            _uiState.value = ResultUiState.Success(channelsList.toList())
        }
    }

    fun onTopicClick(topicName: String, parent: Int) {
        _navigateToChat.value = TopicTransferModel(topicName, parent)
    }

    fun navigateToChatCompleted() {
        _navigateToChat.value = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun query() {
        _queryFlow
            .onEach { _uiState.value = ResultUiState.Loading }
            .flatMapLatest { queryText ->
                flow {
                    delay(500)
                    if (isSubscribedOnly) {
                        emit(repository.querySubscribedChannels(queryText))

                    } else {
                        emit(
                            repository.queryChannels(queryText)
                        )
                    }
                }

            }
            .map { channelModel ->
                channelModel.toPageItem()
            }
            .onEach {
                channelsList.clear()
                channelsList.addAll(it)
            }
            .flowOn(Dispatchers.IO)
            .catch { throwable ->
                throwable.message?.let {
                    _uiState.value = ResultUiState.Error(it)
                }
            }
            .collect { list ->
                _uiState.value = ResultUiState.Success(list)
            }
    }

    private fun List<ChannelUiModel>.toPageItem() =
        map {
            PageItem.ChannelItem(it)
        }

    fun sendQuery(queryText: String) {
        _queryFlow.value = queryText
    }
}
