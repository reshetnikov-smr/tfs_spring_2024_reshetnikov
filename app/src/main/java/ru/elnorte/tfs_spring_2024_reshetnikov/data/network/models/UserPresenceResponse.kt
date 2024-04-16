package ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserPresenceResponse(
    @Json(name = "result")
    val result: String, // success
    @Json(name = "msg")
    val msg: String,
    @Json(name = "presence")
    val presence: Presence
) {
    @JsonClass(generateAdapter = true)
    data class Presence(
        @Json(name = "aggregated")
        val aggregated: Aggregated,
        @Json(name = "website")
        val website: Website
    ) {
        @JsonClass(generateAdapter = true)
        data class Aggregated(
            @Json(name = "status")
            val status: String, // idle
            @Json(name = "timestamp")
            val timestamp: Int // 1713029121
        )

        @JsonClass(generateAdapter = true)
        data class Website(
            @Json(name = "status")
            val status: String, // idle
            @Json(name = "timestamp")
            val timestamp: Int // 1713029121
        )
    }
}
