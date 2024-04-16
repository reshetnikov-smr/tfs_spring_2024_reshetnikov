package ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsersPresenceResponse(
    @Json(name = "result")
    val result: String, // success
    @Json(name = "msg")
    val msg: String,
    @Json(name = "presences")
    val presences: Map<String, UsersPresenceDetails>,
    @Json(name = "server_timestamp")
    val serverTimestamp: Double // 1713030938.133071
) {
    @JsonClass(generateAdapter = true)
    data class UsersPresenceDetails(
        @Json(name = "aggregated")
        val aggregated: Aggregated,
        @Json(name = "website")
        val website: Website
    ) {
        @JsonClass(generateAdapter = true)
        data class Aggregated(
            @Json(name = "client")
            val client: String, // website
            @Json(name = "status")
            val status: String, // active
            @Json(name = "timestamp")
            val timestamp: Int // 1712686691
        )

        @JsonClass(generateAdapter = true)
        data class Website(
            @Json(name = "client")
            val client: String, // website
            @Json(name = "status")
            val status: String, // active
            @Json(name = "timestamp")
            val timestamp: Int, // 1712686691
            @Json(name = "pushable")
            val pushable: Boolean // false
        )
    }

}
