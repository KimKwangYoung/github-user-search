package kky.flab.last_mission.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun LoadableLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    onLoad: () -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    state.onLoadMore(action = onLoad)

    LazyColumn(modifier = modifier, state = state, content = content)
}

private fun LazyListState.reachedBottom(
    limitCount: Int = 6,
    triggerOnEnd: Boolean = false,
): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return (
            (triggerOnEnd && lastVisibleItem?.index == layoutInfo.totalItemsCount - 1)
            || lastVisibleItem?.index != null
            && lastVisibleItem.index >= layoutInfo.totalItemsCount - (limitCount + 2)
        )
}

@SuppressLint("ComposableNaming")
@Composable
private fun LazyListState.onLoadMore(
    limitCount: Int = 4,
    loadOnBottom: Boolean = true,
    action: () -> Unit,
) {
    val reached by remember {
        derivedStateOf {
            reachedBottom(
                limitCount = limitCount,
                triggerOnEnd = loadOnBottom
            )
        }
    }

    LaunchedEffect(reached) {
        if (reached) {
            action()
        }
    }
}
