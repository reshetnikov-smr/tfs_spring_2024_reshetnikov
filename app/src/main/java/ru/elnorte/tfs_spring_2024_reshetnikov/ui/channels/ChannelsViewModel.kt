package ru.elnorte.tfs_spring_2024_reshetnikov.ui.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChannelsViewModel : ViewModel() {

    private var currentPosition: Int = 0

    private val _queryTextFlow = MutableStateFlow("")

    private val _queryText = MutableLiveData<QueryPageParams>()
    val queryText: LiveData<QueryPageParams> = _queryText

    init {
        viewModelScope.launch {
            handleQuery()
        }
    }

    fun sendQueryFlow(queryText: String) {
        _queryTextFlow.value = queryText
    }

    fun changePosition(position: Int) {
        if (currentPosition != position) {
            currentPosition = position
            val text = queryText.value?.text.orEmpty()
            _queryText.value = QueryPageParams(text, position)
        }
    }

    @OptIn(FlowPreview::class)
    private suspend fun handleQuery() {
        _queryTextFlow
            .debounce(2000)
            .distinctUntilChanged()
            .map {
                QueryPageParams(it, currentPosition)
            }
            .collect {
                _queryText.value = it
            }
    }
}

data class QueryPageParams(
    val text: String, val position: Int,
)
