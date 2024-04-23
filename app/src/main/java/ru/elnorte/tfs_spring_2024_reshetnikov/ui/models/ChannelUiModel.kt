package ru.elnorte.tfs_spring_2024_reshetnikov.ui.models

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PageItem

data class ChannelUiModel(
    val channelId: Int,
    val name: String,
    val isExpanded: Boolean = false,
)

fun List<ChannelUiModel>.toPageItem() =
    map {
        PageItem.ChannelItem(it)
    }
