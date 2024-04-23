package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore

class TopicStore(reducer: TopicReducer, actor: TopicActor) :
    MviStore<TopicPartialState, TopicIntent, TopicState, TopicEffect>(reducer, actor) {
    override fun initialStateCreator(): TopicState = TopicState(TopicUiState.Loading)

    init {
        initialStateCreator()
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                postIntent(TopicIntent.ReloadPage)

            }
        }
    }
}
