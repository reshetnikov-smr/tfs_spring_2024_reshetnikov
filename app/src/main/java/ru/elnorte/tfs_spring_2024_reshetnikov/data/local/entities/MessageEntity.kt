package ru.elnorte.tfs_spring_2024_reshetnikov.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages_table")
data class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    val messageId: Int,
    val timestamp: Int,
    @ColumnInfo(name = "sender_id")
    val senderId: Int,
    @ColumnInfo(name = "is_mine_message")
    val isMineMessage: Boolean,
    @ColumnInfo(name = "sender_avatar")
    val senderAvatar: String?,
    @ColumnInfo(name = "user_name")
    val userName: String?,
    val content: String?,
    @ColumnInfo(name = "parent_name")
    val parentName: String?,
    @ColumnInfo(name = "channel_id")
    val channelId: Int,
)
