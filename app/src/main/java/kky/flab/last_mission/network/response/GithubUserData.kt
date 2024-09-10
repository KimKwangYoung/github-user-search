package kky.flab.last_mission.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubUserData(
    val id: Long,
    @SerialName("login") val name: String,
    @SerialName("avatar_url") val imageUrl: String,
)
