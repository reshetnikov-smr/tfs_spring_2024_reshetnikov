package ru.elnorte.tfs_spring_2024_reshetnikov.ui.models

data class PersonUiModel(
    val personId: Int,
    val name: String,
    val email: String,
    val status: String,
    val isOnline: String,
    val avatar: Int?,
)
