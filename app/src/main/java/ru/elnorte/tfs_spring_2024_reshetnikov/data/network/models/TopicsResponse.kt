package ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.elnorte.tfs_spring_2024_reshetnikov.data.models.TopicDatabaseModel

@JsonClass(generateAdapter = true)
data class TopicsResponse(
    @Json(name = "result")
    val result: String, // success
    @Json(name = "msg")
    val msg: String,
    @Json(name = "topics")
    val topics: List<Topic>
) {
    @JsonClass(generateAdapter = true)
    data class Topic(
        @Json(name = "name")
        val name: String, // testing
        @Json(name = "max_id")
        val maxId: Int // 433134899
    )
}

fun TopicsResponse.asTopicsDatabaseModel(): List<TopicDatabaseModel> {
    val output = mutableListOf<TopicDatabaseModel>()
    this.topics.forEach {
        output.add(TopicDatabaseModel(0, it.name, 0, "#2A9D8F"))
    }
    return output
}
