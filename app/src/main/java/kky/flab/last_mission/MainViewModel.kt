@file:OptIn(FlowPreview::class)

package kky.flab.last_mission

import android.util.Log
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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
): ViewModel() {

    private val _searchState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)
    val searchState: StateFlow<MainUiState> = _searchState.asStateFlow()

    private val _keywordFlow: MutableStateFlow<String> = MutableStateFlow("")
    val keywordFlow: StateFlow<String> = _keywordFlow.asStateFlow()

    init {
       collectKeywordFlow()
    }

    fun collectKeywordFlow() = flow { emit(true) }
        .flatMapLatest { _keywordFlow }
        .debounce(200)
        .flatMapLatest { keyword ->
            Log.d("MainViewModel", "step1")
            githubUserSearchRepository.searchUser(keyword)
        }
        .retryWhen { cause, attempt ->
            Log.e("MainViewModel", cause.javaClass.toString())
            when(cause) {
                is SocketTimeoutException -> attempt < 1
                is UnknownHostException -> attempt < 1
                is HttpException -> false
                else -> false
            }
        }
        .catch {
            when(it) {
                is SocketTimeoutException,
                is UnknownHostException -> {
                    _searchState.value = MainUiState.Fail("네트워크 상태를 확인해주세요.")
                }
                is HttpException -> {
                    _searchState.value = MainUiState.Fail(it.message ?: "서버 에러")
                }
                else -> {
                    _searchState.value = MainUiState.Fail("알 수 없는 에러가 발생했습니다.")
                }
            }
        }
        .onEach { users ->
            _searchState.value = MainUiState.Success(users)
        }
        .launchIn(viewModelScope)

    fun onChangeKeyword(keyword: String) {
        _keywordFlow.value = keyword
    }

    fun saveMemo(user: GithubUser, memo: String) {
        githubUserSearchRepository.memo(user.id, memo)
    }

    fun removeUser(user: GithubUser) {
        githubUserSearchRepository.remove(user.id)
    }
}
