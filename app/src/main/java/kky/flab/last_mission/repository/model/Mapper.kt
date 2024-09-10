package kky.flab.last_mission.repository.model

import kky.flab.last_mission.network.response.GithubUserData

fun GithubUserData.toDomain(memo: String): GithubUser =
    GithubUser(
        id = id,
        name = name,
        imageUrl = imageUrl,
        memo = memo,
    )