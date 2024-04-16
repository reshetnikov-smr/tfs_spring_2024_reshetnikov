package ru.elnorte.tfs_spring_2024_reshetnikov.data.models

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel

data class TopicDatabaseModel(
    var topicId: Int,
    var name: String,
    var messagesCount: Int,
    var color: String,
)

fun TopicDatabaseModel.asTopicUiModel(channelId: Int): TopicUiModel {
    return TopicUiModel(
        this.topicId,
        channelId,
        this.name,
        "${this.messagesCount} mes",
        this.color

    )
}

fun List<TopicDatabaseModel>.asTopicUiModel(channelId: Int): List<TopicUiModel> {
    return this.map { it.asTopicUiModel(channelId) }
}
