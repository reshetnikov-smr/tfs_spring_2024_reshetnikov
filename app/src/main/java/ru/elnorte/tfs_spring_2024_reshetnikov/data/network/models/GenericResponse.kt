package ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenericResponse(
    @Json(name = "msg")
    val msg: String,
    @Json(name = "result")
    val result: String // success
)
