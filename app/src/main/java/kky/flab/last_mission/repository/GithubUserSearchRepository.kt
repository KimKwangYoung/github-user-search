package kky.flab.last_mission.repository

import kky.flab.last_mission.repository.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUserSearchRepository {
    fun flowSearchUser(keyword: String): Flow<List<GithubUser>>

    fun remove(id: Long)

    fun memo(id: Long, memo: String)
}
