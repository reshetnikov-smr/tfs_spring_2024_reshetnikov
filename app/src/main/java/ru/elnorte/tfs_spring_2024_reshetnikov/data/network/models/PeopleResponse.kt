package ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.OnlineStatus
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel

@JsonClass(generateAdapter = true)
data class PeopleResponse(
    @Json(name = "result")
    val result: String, // success
    @Json(name = "msg")
    val msg: String,
    @Json(name = "members")
    val members: List<Member>
) {
    @JsonClass(generateAdapter = true)
    data class Member(
        @Json(name = "email")
        val email: String, // user709777@tinkoff-android-spring-2024.zulipchat.com
        @Json(name = "user_id")
        val userId: Int, // 709777
        @Json(name = "avatar_version")
        val avatarVersion: Int, // 1
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
        val fullName: String, // Ilya Bekmansurov
        @Json(name = "timezone")
        val timezone: String, // Europe/Moscow
        @Json(name = "is_active")
        val isActive: Boolean, // true
        @Json(name = "date_joined")
        val dateJoined: String, // 2024-04-13T08:45:24.664884+00:00
        @Json(name = "delivery_email")
        val deliveryEmail: String?, // elnortemobile@gmail.com
        @Json(name = "avatar_url")
        val avatarUrl: String? // https://secure.gravatar.com/avatar/7d1ac4b3194f3da9c1e92ce2b5390bf9?d=identicon&version=1
    )
}


fun UsersMapper(people: PeopleResponse, presence: UsersPresenceResponse): List<PersonUiModel> {
    val output = mutableListOf<PersonUiModel>()
    people.members.forEach {
        val lastAction =
            presence.serverTimestamp - (presence.presences[it.email]?.website?.timestamp ?: 0)
        val onlineStatus = when {
            presence.presences[it.email]?.website?.status == "active" && lastAction < 300 -> OnlineStatus.Online
            presence.presences[it.email]?.website?.status == "idle" && lastAction < 300 -> OnlineStatus.Idle
            else -> OnlineStatus.Offline
        }
        output.add(
            PersonUiModel(
                it.userId,
                it.fullName,
                it.email,
                "",
                onlineStatus,
                it.avatarUrl
            )
        )
    }
    return output
}

fun PeopleResponse.asPersonUI(): List<PersonUiModel> {
    val output = mutableListOf<PersonUiModel>()
    this.members.forEach {
        output.add(
            PersonUiModel(
                it.userId,
                it.fullName,
                it.email,
                "",
                OnlineStatus.Online,
                it.avatarUrl
            )
        )
    }
    return output
}
