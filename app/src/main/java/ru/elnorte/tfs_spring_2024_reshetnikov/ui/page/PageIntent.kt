package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviIntent

sealed interface PageIntent : MviIntent {
    data class SendQuery(val queryText: String, val isSubscribedOnly: Boolean) : PageIntent
    data class ChannelItemClick(val channelId: Int) : PageIntent
    data class Init(val isSubscribedOnly: Boolean) : PageIntent
}
