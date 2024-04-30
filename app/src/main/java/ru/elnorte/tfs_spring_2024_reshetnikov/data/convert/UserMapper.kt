package ru.elnorte.tfs_spring_2024_reshetnikov.data.convert

import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.PeopleResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.data.network.models.UsersPresenceResponse
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.OnlineStatus
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel

class UserMapper {
    fun convert(people: PeopleResponse, presence: UsersPresenceResponse): List<PersonUiModel> {
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
}
