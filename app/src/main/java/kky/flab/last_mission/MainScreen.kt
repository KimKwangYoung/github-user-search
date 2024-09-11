package kky.flab.last_mission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kky.flab.last_mission.repository.model.GithubUser
import kky.flab.last_mission.ui.component.GithubUserItem
import kky.flab.last_mission.ui.model.MainUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val keyword = viewModel.keywordFlow.collectAsStateWithLifecycle().value
    val state = viewModel.searchState.collectAsStateWithLifecycle().value

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
                onChangeKeyword = viewModel::onChangeKeyword,
            )
            when (state) {
                is MainUiState.Fail -> FailContent(
                    message = state.message,
                    onRetry = { viewModel.collectKeywordFlow() },
                )

                MainUiState.Loading -> { /* */ }

                is MainUiState.Success -> {
                    GithubUserList(
                        data = state.data,
                        onSaveMemo = viewModel::saveMemo,
                        onClickItem = viewModel::removeUser,
                    )
                }
            }
        }
    }
}


@Composable
fun SearchBar(
    onChangeKeyword: (String) -> Unit,
    keyword: String,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = keyword,
        onValueChange = onChangeKeyword,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color.Black,
                contentDescription = ""
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .background(Color.Transparent)
    )
}

@Composable
fun GithubUserList(
    data: List<GithubUser>,
    onClickItem: (GithubUser) -> Unit,
    onSaveMemo: (GithubUser, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(data) { user ->
            GithubUserItem(
                user = user,
                onSaveMemo = { memo -> onSaveMemo(user, memo) },
                onClickItem = { onClickItem(user) },
            )
        }
    }
}

@Composable
fun FailContent(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(5.dp),
        ) {
            Text(
                text = "재시도",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}