package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.IMessengerRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel

class ContactsViewModel(repository: IMessengerRepository) : ViewModel() {

    private val _peopleModel = MutableLiveData<List<PersonUiModel>>()
    val peopleModel: LiveData<List<PersonUiModel>>
        get() = _peopleModel

    init {
        _peopleModel.value = repository.getPeople()
    }
}
