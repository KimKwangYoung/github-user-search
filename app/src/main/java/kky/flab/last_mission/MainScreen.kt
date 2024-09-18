package kky.flab.last_mission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kky.flab.last_mission.repository.model.GithubUser
import kky.flab.last_mission.ui.component.SearchBar
import kky.flab.last_mission.ui.component.SearchFail
import kky.flab.last_mission.ui.component.SearchResult
import kky.flab.last_mission.ui.model.MainUiState

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val keyword = viewModel.keywordFlow.collectAsStateWithLifecycle().value
    val state = viewModel.searchState.collectAsStateWithLifecycle().value

    MainContent(
        uiState = state,
        keyword = keyword,
        onChangeKeyword = viewModel::onChangeKeyword,
        onRetry = viewModel::retry,
        onSaveMemo = viewModel::saveMemo,
        onClickItem = viewModel::removeUser,
        onLoadMore = viewModel::loadMore
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    uiState: MainUiState,
    keyword: String,
    onChangeKeyword: (String) -> Unit,
    onRetry: () -> Unit,
    onSaveMemo: (GithubUser, String) -> Unit,
    onClickItem: (GithubUser) -> Unit,
    onLoadMore: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "GithubUserSearch") })
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                keyword = keyword,
                onChangeKeyword = onChangeKeyword,
            )
            when (uiState) {
                is MainUiState.Fail -> SearchFail(
                    message = uiState.message,
                    onRetry = onRetry
                )

                MainUiState.Loading -> { /* */ }

                is MainUiState.Success -> {
                    SearchResult(
                        data = uiState.data,
                        onSaveMemo = onSaveMemo,
                        onClickItem = onClickItem,
                        onLoadMore = onLoadMore,
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
fun MainScreenResultPreview() {
    MainContent(
        uiState = MainUiState.Success(
            data = listOf(
                GithubUser(
                    id = 0,
                    name = "Preview User",
                    memo = "preview",
                    imageUrl = ""
                ),
                GithubUser(
                    id = 0,
                    name = "Preview User2",
                    memo = "preview2",
                    imageUrl = ""
                ),
            )
        ),
        keyword = "keyword",
        onChangeKeyword = {},
        onRetry = {},
        onSaveMemo = { _, _ -> },
        onClickItem = {},
        onLoadMore = {}
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
fun MainScreenFailPreview() {
    MainContent(
        uiState = MainUiState.Fail("네트워크 상태를 확인해주세요."),
        keyword = "keyword",
        onChangeKeyword = {},
        onRetry = {},
        onSaveMemo = { _, _ -> },
        onClickItem = {},
        onLoadMore = {}
    )
}
