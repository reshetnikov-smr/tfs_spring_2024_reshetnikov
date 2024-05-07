package ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel

@Entity(tableName = "topics_table")
data class TopicEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    @ColumnInfo(name = "messages_count")
    var messagesCount: Int = 0,
    @ColumnInfo(name = "parent_id")
    var parentId: Int,
)

fun TopicEntity.asTopicUiModel(): TopicUiModel {
    return TopicUiModel(
        this.id,
        this.parentId,
        this.name,
        "${this.messagesCount} mes",
        "#2A9D8F"
    )
}

fun List<TopicEntity>.asTopicUiModel(): List<TopicUiModel> {
    return this.map { it.asTopicUiModel() }
}
