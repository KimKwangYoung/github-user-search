@file:OptIn(FlowPreview::class)

package kky.flab.last_mission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kky.flab.last_mission.repository.GithubUserSearchRepository
import kky.flab.last_mission.repository.model.GithubUser
import kky.flab.last_mission.ui.model.MainUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val githubUserSearchRepository: GithubUserSearchRepository
) : ViewModel() {

    private val _searchState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)
    val searchState: StateFlow<MainUiState> = _searchState.asStateFlow()

    private val _keywordFlow: MutableStateFlow<String> = MutableStateFlow("")
    val keywordFlow: StateFlow<String> = _keywordFlow.asStateFlow()

    private val _retry: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _loadMore: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        collectKeywordFlow()
    }

    private fun collectKeywordFlow() = _retry
        .filter { it }
        .onEach { _retry.value = false }
        .flatMapLatest { keywordFlow }
        .debounce(200)
        .flatMapLatest { keyword -> githubUserSearchRepository.flowSearchUser(keyword) }
        .retryWhen { cause, _ ->
            when (cause) {
                is SocketTimeoutException, is UnknownHostException -> {
                    _searchState.value = MainUiState.Fail("네트워크 상태를 확인해주세요.")
                }

                is HttpException -> {
                    _searchState.value = MainUiState.Fail(cause.message ?: "서버 에러")
                }

                else -> {
                    _searchState.value = MainUiState.Fail("알 수 없는 에러가 발생했습니다.")
                }
            }
            true
        }
        .onEach { users ->
            _searchState.value = MainUiState.Success(users)
        }
        .launchIn(viewModelScope)

    fun retry() {
        _retry.value = true
    }

    fun onChangeKeyword(keyword: String) {
        if (_retry.value.not()) {
            _retry.value = true
        }

        _keywordFlow.value = keyword
    }

    fun saveMemo(user: GithubUser, memo: String) {
        githubUserSearchRepository.memo(
            user.id,
            memo
        )
    }

    fun removeUser(user: GithubUser) {
        githubUserSearchRepository.remove(user.id)
    }
}
