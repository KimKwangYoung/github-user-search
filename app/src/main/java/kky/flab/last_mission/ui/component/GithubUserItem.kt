package kky.flab.last_mission.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClickItem)
            .padding(16.dp)
    ) {
        GithubImage(
            imageSource = user.imageUrl,
            shape = GithubImageShape.Circle,
            modifier = Modifier.size(60.dp),
            error = painterResource(id = R.drawable.profile_error),
            loading = painterResource(id = R.drawable.profile_loading)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1.0f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (editMode) {
                InputMemoRow(
                    value = textFieldState,
                    onValueChange = { textFieldState = it },
                    onSaveMemo = {
                        editMode = false
                        onSaveMemo(textFieldState)
                    }
                )
            } else {
                Text(
                    text = user.memo,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        IconButton(onClick = { editMode = true }) {
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
    onValueChange: (String) -> Unit,
    onSaveMemo: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        MemoTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1.0f)
        )
        OutlinedButton(
            onClick = onSaveMemo,
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
            modifier = Modifier
                .defaultMinSize(
                    minWidth = 1.dp,
                    minHeight = 1.dp,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        textStyle = MaterialTheme.typography.bodySmall,
        onValueChange = onValueChange,
        modifier = modifier,
    ) {
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = it,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Black,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Transparent,
            ),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
        )
    }
}