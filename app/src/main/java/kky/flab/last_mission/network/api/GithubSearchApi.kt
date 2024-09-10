package kky.flab.last_mission.network.api

import kky.flab.last_mission.network.response.GithubSearchResponse
import kky.flab.last_mission.network.response.GithubUserData
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubSearchApi {
    @GET("/search/users")
    suspend fun search(
        @Query("q") keyword: String
    ): GithubSearchResponse<GithubUserData>
}