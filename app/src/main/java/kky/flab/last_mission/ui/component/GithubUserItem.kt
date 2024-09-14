package kky.flab.last_mission.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kky.flab.last_mission.R
import kky.flab.last_mission.repository.model.GithubUser

@Composable
fun GithubUserItem(
    user: GithubUser,
    onSaveMemo: (String) -> Unit,
    onClickItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var textFieldState by remember(user.id) { mutableStateOf(user.memo) }
    var editMode by remember(user.id) { mutableStateOf(false) }

    GithubUserItem(
        data = user,
        textState = textFieldState,
        editMode = editMode,
        onClickItem = onClickItem,
        modifier = modifier,
        onToggleEditMode = { editMode = editMode.not() },
        onTextValueChanged = { value -> textFieldState = value },
        onSaveMemo = {
            editMode = false
            onSaveMemo(textFieldState)
        },
    )
}

@Composable
private fun GithubUserItem(
    data: GithubUser,
    textState: String,
    editMode: Boolean,
    onSaveMemo: () -> Unit,
    onClickItem: () -> Unit,
    onTextValueChanged: (String) -> Unit,
    onToggleEditMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClickItem)
            .padding(16.dp),
    ) {
        GithubImage(
            imageSource = data.imageUrl,
            shape = GithubImageShape.Circle,
            modifier = Modifier.size(60.dp),
            error = painterResource(id = R.drawable.profile_error),
            loading = painterResource(id = R.drawable.profile_loading)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = data.name,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (editMode) {
                InputMemoRow(
                    value = textState,
                    onValueChange = onTextValueChanged,
                    onSaveMemo = { onSaveMemo() },
                )
            } else {
                Text(
                    text = data.memo,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            onClick = onToggleEditMode,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        }
    }
}

@Composable
fun InputMemoRow(
    value: String,
    onSaveMemo: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        UnderBarTextField(
            value = value,
            modifier = Modifier.weight(1.0f),
            onValueChange = onValueChange,
        )
        OutlinedButton(
            onClick = onSaveMemo,
            contentPadding = PaddingValues(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            modifier = Modifier
                .defaultMinSize(
                    minWidth = 1.dp,
                    minHeight = 1.dp
                )
                .padding(start = 10.dp),
        ) {
            Text(
                text = "저장",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
fun InputMemoRowPreview() {
    InputMemoRow(value = "",
        onValueChange = {},
        onSaveMemo = {})
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun GithubUserItemPreview() {
    Column {
        GithubUserItem(
            data = GithubUser(
                id = 0,
                name = "GithubUser1",
                memo = "preview memo",
                imageUrl = ""
            ),
            textState = "",
            editMode = false,
            onSaveMemo = {},
            onClickItem = {},
            onTextValueChanged = {},
            onToggleEditMode = {},
        )
        GithubUserItem(
            data = GithubUser(
                id = 0,
                name = "GithubUser2",
                memo = "preview memo",
                imageUrl = ""
            ),
            textState = "edit memo preview",
            editMode = true,
            onSaveMemo = {},
            onClickItem = {},
            onTextValueChanged = {},
            onToggleEditMode = {},
        )
    }
}
