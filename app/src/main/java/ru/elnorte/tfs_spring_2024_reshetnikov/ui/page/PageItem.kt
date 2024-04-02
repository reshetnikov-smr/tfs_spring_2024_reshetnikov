package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel

sealed class PageItem(val id: Int) {

    data class TopicItem(val topic: TopicUiModel) : PageItem(topic.topicId)
    data class ChannelItem(val channel: ChannelUiModel) : PageItem(channel.channelId)
}
