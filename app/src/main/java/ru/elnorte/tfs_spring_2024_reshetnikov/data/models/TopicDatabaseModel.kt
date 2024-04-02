package ru.elnorte.tfs_spring_2024_reshetnikov.data.models

import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel

data class TopicDatabaseModel(
    var topicId: Int,
    var name: String,
    var messagesCount: Int,
    var color: String,
)

fun TopicDatabaseModel.asTopicUiModel(): TopicUiModel {
    return TopicUiModel(
        this.topicId,
        this.name,
        "${this.messagesCount} mes",
        this.color

    )
}
