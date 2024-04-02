package ru.elnorte.tfs_spring_2024_reshetnikov.data.models

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel

data class ChannelDatabaseModel(
    val channelId: Int,
    val channelName: String,
    val channelDescription: String,
    val topicsList: List<Int>,
)

fun ChannelDatabaseModel.asChannelUiModel(): ChannelUiModel {
    return ChannelUiModel(
        channelId = this.channelId,
        name = channelName,
    )
}
