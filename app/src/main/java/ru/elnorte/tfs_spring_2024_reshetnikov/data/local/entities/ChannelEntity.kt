package ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel

@Entity(tableName = "channels_table")
class ChannelEntity(
    @PrimaryKey
    @ColumnInfo(name = "channel_id")
    val channelId: Int,
    val name: String,
    val description: String,
)

fun ChannelEntity.asChannelUiModel(): ChannelUiModel {
    return ChannelUiModel(
        channelId = this.channelId,
        name = name,
    )
}

fun List<ChannelEntity>.asChannelUiModel(): List<ChannelUiModel> {
    return this.map { it.asChannelUiModel() }
}
