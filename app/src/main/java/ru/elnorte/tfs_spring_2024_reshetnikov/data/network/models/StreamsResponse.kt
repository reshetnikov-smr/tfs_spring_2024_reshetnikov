package ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.ChannelDatabaseModel

@JsonClass(generateAdapter = true)
data class StreamsResponse(
    @Json(name = "result")
    val result: String, // success
    @Json(name = "msg")
    val msg: String,
    @Json(name = "streams")
    val streams: List<Stream>
) {
    @JsonClass(generateAdapter = true)
    data class Stream(
        @Json(name = "can_remove_subscribers_group")
        val canRemoveSubscribersGroup: Int, // 505981
        @Json(name = "date_created")
        val dateCreated: Int, // 1712657867
        @Json(name = "description")
        val description: String, // Everyone is added to this stream by default. Welcome! :octopus:
        @Json(name = "first_message_id")
        val firstMessageId: Int, // 432191838
        @Json(name = "history_public_to_subscribers")
        val historyPublicToSubscribers: Boolean, // true
        @Json(name = "invite_only")
        val inviteOnly: Boolean, // false
        @Json(name = "is_web_public")
        val isWebPublic: Boolean, // false
        @Json(name = "message_retention_days")
        val messageRetentionDays: Any?, // null
        @Json(name = "name")
        val name: String, // general
        @Json(name = "rendered_description")
        val renderedDescription: String, // <p>Everyone is added to this stream by default. Welcome! <span aria-label="octopus" class="emoji emoji-1f419" role="img" title="octopus">:octopus:</span></p>
        @Json(name = "stream_id")
        val streamId: Int, // 432915
        @Json(name = "stream_post_policy")
        val streamPostPolicy: Int, // 1
        @Json(name = "is_announcement_only")
        val isAnnouncementOnly: Boolean, // false
        @Json(name = "stream_weekly_traffic")
        val streamWeeklyTraffic: Any? // null
    )
}

fun StreamsResponse.asChannelsDatabaseModel(): List<ChannelDatabaseModel> {
    val output = mutableListOf<ChannelDatabaseModel>()
    this.streams.forEach {
        output.add(ChannelDatabaseModel(it.streamId, it.name, it.description, listOf()))
    }
    return output
}
