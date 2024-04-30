package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.OnlineStatus
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviEffect

sealed interface ContactsEffect : MviEffect {
    data class ShowError(val message: String) : ContactsEffect

    data class NavigateToPerson(
        val avatar: String?,
        val name: String,
        val status: String,
        val isOnline: OnlineStatus,
    ) : ContactsEffect
}
