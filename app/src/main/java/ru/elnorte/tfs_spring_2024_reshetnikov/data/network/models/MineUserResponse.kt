package ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.OnlineStatus
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel

@JsonClass(generateAdapter = true)
data class MineUserResponse(
    @Json(name = "result")
    val result: String, // success
    @Json(name = "msg")
    val msg: String,
    @Json(name = "email")
    val email: String, // user709775@tinkoff-android-spring-2024.zulipchat.com
    @Json(name = "user_id")
    val userId: Int, // 709775
    @Json(name = "avatar_version")
    val avatarVersion: Int, // 2
    @Json(name = "is_admin")
    val isAdmin: Boolean, // false
    @Json(name = "is_owner")
    val isOwner: Boolean, // false
    @Json(name = "is_guest")
    val isGuest: Boolean, // false
    @Json(name = "is_billing_admin")
    val isBillingAdmin: Boolean, // false
    @Json(name = "role")
    val role: Int, // 400
    @Json(name = "is_bot")
    val isBot: Boolean, // false
    @Json(name = "full_name")
    val fullName: String, // Алексей Решетников
    @Json(name = "timezone")
    val timezone: String, // Europe/Samara
    @Json(name = "is_active")
    val isActive: Boolean, // true
    @Json(name = "date_joined")
    val dateJoined: String, // 2024-04-13T08:38:15.918124+00:00
    @Json(name = "delivery_email")
    val deliveryEmail: String, // elnortemobile@gmail.com
    @Json(name = "avatar_url")
    val avatarUrl: String, // https://zulip-avatars.s3.amazonaws.com/63414/a6fcc9d57f63284a522bad3c0ac582035af2ee58?version=2
    @Json(name = "profile_data")
    val profileData: ProfileData,
    @Json(name = "max_message_id")
    val maxMessageId: Int // 433572112
) {
    @JsonClass(generateAdapter = true)
    class ProfileData
}


fun MineUserResponse.asPersonUiModel(): PersonUiModel {
    return PersonUiModel(
        this.userId,
        this.fullName,
        this.email,
        "",
        OnlineStatus.Online,
        this.avatarUrl
    )
}
