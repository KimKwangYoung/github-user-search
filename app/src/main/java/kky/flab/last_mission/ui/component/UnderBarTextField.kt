package kky.flab.last_mission.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnderBarTextField(
    value: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    onValueChange: (String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        textStyle = style,
        onValueChange = onValueChange,
        modifier = modifier,
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Black,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Transparent,
            ),
            contentPadding = PaddingValues(
                horizontal = 0.dp,
                vertical = 0.dp
            ),
        )
    }
}

@Preview(
    widthDp = 200,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
fun UnderBarTextFieldPreview() {
    UnderBarTextField(value = "preview") {}
}
