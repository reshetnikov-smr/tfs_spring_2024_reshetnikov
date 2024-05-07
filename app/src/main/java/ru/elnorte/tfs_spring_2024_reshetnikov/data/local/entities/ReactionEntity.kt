package ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "reactions_table", primaryKeys = ["emoji_code", "sender_id", "message_id"])
data class ReactionEntity(

    @ColumnInfo(name = "emoji_code")
    var emojiCode: String,
    @ColumnInfo(name = "sender_id")
    var senderId: Int,
    @ColumnInfo(name = "message_id")
    var messageId: Int,
)
