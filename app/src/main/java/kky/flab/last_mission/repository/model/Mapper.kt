package kky.flab.last_mission.repository.model

import kky.flab.last_mission.network.response.GithubUserData

fun GithubUserData.toDomain(): GithubUser =
    GithubUser(
        id = id,
        name = name,
        imageUrl = imageUrl,
        memo = "",
    )