package ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageSendingResponse(
    @Json(name = "automatic_new_visibility_policy")
    val automaticNewVisibilityPolicy: Int?, // 2
    val id: Int?, // 42
    val msg: String,
    val result: String,// success
    val stream: String?, // success
    val code: String?,
)
