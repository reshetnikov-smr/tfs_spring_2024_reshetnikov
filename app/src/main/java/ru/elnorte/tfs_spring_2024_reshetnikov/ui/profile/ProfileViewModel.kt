package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.IMessengerRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel

class ProfileViewModel(repository: IMessengerRepository) : ViewModel() {

    private val _personModel = MutableLiveData<PersonUiModel>()
    val personModel: LiveData<PersonUiModel>
        get() = _personModel

    init {
        _personModel.value = repository.getMe()
    }
}
