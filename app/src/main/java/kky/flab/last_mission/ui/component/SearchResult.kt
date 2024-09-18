package kky.flab.last_mission.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kky.flab.last_mission.repository.model.GithubUser

@Composable
fun SearchResult(
    data: List<GithubUser>,
    onClickItem: (GithubUser) -> Unit,
    onSaveMemo: (GithubUser, String) -> Unit,
    onLoadMore: () -> Unit
) {
    LoadableLazyColumn(
        modifier = Modifier.fillMaxSize(),
        onLoad = onLoadMore
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

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
fun GithubUserListPreview() {
    SearchResult(
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
            )
        ),
        onClickItem = {},
        onSaveMemo = { _, _ -> },
        onLoadMore = {}
    )
}
