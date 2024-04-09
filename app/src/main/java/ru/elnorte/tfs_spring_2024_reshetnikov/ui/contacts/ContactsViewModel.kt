package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.IMessengerRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.QueryResultUiState

class ContactsViewModel(private val repository: IMessengerRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<QueryResultUiState<PersonUiModel>> =
        MutableStateFlow(QueryResultUiState.Loading)

    val uiState: StateFlow<QueryResultUiState<PersonUiModel>> = _uiState

    private val queryTextFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            handleQuery()
        }
    }

    fun sendQueryFlow(queryText: String) {
        queryTextFlow.value = queryText
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private suspend fun handleQuery() {
        queryTextFlow
            .onEach { _uiState.value = QueryResultUiState.Loading }
            .debounce(2000)
            .distinctUntilChanged()
            .flatMapLatest { queryText ->
                flow {
                    emit(repository.queryContacts(queryText))
                }
            }
            .flowOn(Dispatchers.IO)
            .collect { list ->
                _uiState.value = QueryResultUiState.Success(list)
            }
    }
}
