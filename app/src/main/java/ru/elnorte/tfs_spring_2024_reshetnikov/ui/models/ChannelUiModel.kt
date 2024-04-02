package ru.elnorte.tfs_spring_2024_reshetnikov.ui.models

data class ChannelUiModel(
    val channelId: Int,
    val name: String,
    val isExpanded: Boolean = false,
)
