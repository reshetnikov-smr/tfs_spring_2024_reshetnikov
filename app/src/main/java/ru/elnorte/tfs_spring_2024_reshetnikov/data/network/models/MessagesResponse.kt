package ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response by request
 * GET https://yourZulipDomain.zulipchat.com/api/v1/messages
 */
@JsonClass(generateAdapter = true)
data class MessagesResponse(
    val anchor: Long, // 21
    @Json(name = "found_anchor")
    val foundAnchor: Boolean, // true
    @Json(name = "found_newest")
    val foundNewest: Boolean, // true
    val messages: List<MessageResponse>,
    val msg: String,
    val result: String // success
)

@JsonClass(generateAdapter = true)
data class MessageResponse(
    @Json(name = "avatar_url")
    val avatarUrl: String, // https://secure.gravatar.com/avatar/6d8cad0fd00256e7b40691d27ddfd466?d=identicon&version=1
    val client: String, // populate_db
    val content: String, // <p>Security experts agree that relational algorithms are an interesting new topic in the field of networking, and scholars concur.</p>
    @Json(name = "content_type")
    val contentType: String, // text/html
    @Json(name = "display_recipient")
    val displayRecipient: String,
    val flags: List<String>,
    val id: Int, // 16
    @Json(name = "is_me_message")
    val isMeMessage: Boolean, // false
    val reactions: List<ReactionResponse>,
    @Json(name = "recipient_id")
    val recipientId: Int, // 27
    @Json(name = "sender_email")
    val senderEmail: String, // hamlet@zulip.com
    @Json(name = "sender_full_name")
    val senderFullName: String, // King Hamlet
    @Json(name = "sender_id")
    val senderId: Int, // 4
    @Json(name = "sender_realm_str")
    val senderRealmStr: String, // zulip
    @Json(name = "stream_id")
    val streamId: Int?, // 5
    val subject: String,
    val submessages: List<Any>,
    val timestamp: Int, // 1527921326
    @Json(name = "topic_links")
    val topicLinks: List<Any>,
    val type: String // private
)

@JsonClass(generateAdapter = true)
data class DisplayRecipient(
    val email: String, // hamlet@zulip.com
    @Json(name = "full_name")
    val fullName: String, // King Hamlet
    val id: Int, // 4
    @Json(name = "is_mirror_dummy")
    val isMirrorDummy: Boolean // false
)

@JsonClass(generateAdapter = true)
data class ReactionResponse(
    @Json(name = "emoji_code")
    val emojiCode: String, // 1f600
    @Json(name = "emoji_name")
    val emojiName: String, // grinning
    @Json(name = "reaction_type")
    val reactionType: String, // unicode_emoji
    val user: User,
    @Json(name = "user_id")
    val userId: Int // 709775
)

@JsonClass(generateAdapter = true)
data class User(
    val email: String, // user709775@tinkoff-android-spring-2024.zulipchat.com
    @Json(name = "full_name")
    val fullName: String, // Алексей Решетников
    val id: Int // 709775
)
