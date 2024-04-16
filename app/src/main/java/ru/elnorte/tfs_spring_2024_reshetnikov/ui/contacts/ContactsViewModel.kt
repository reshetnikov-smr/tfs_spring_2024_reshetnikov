package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.IUserRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState

class ContactsViewModel(private val repository: IUserRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<ResultUiState<PersonUiModel>> =
        MutableStateFlow(ResultUiState.Loading)

    val uiState: StateFlow<ResultUiState<PersonUiModel>> = _uiState

    private val _navigateToPerson = MutableLiveData<Int?>()
    val navigateToPerson: LiveData<Int?>
        get() = _navigateToPerson

    private val queryTextFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            handleQuery()
        }
    }

    fun sendQueryFlow(queryText: String) {
        queryTextFlow.value = queryText
    }

    fun navigateToPersonCompleted() {
        _navigateToPerson.value = null
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private suspend fun handleQuery() {
        queryTextFlow
            .onEach { _uiState.value = ResultUiState.Loading }
            .debounce(500)
            .distinctUntilChanged()
            .flatMapLatest { queryText ->
                flow {
                    emit(repository.queryContacts(queryText))
                }
            }
            .flowOn(Dispatchers.IO)
            .collect { list ->
                _uiState.value = ResultUiState.Success(list)
            }
    }
}
