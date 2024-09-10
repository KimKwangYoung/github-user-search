package kky.flab.last_mission.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubSearchResponse<T>(
    @SerialName("total_count") val totalCount: Int,
    val items: List<T>,
)
