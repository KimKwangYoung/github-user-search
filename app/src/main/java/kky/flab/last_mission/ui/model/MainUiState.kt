package kky.flab.last_mission.ui.model

import kky.flab.last_mission.repository.model.GithubUser

sealed interface MainUiState {
    data object Loading : MainUiState

    data class Fail(
        val message: String
    ) : MainUiState

    data class Success(
        val data: List<GithubUser>
    ) : MainUiState
}
