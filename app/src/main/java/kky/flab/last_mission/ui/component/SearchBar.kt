package kky.flab.last_mission.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(
        onChangeKeyword = {},
        keyword = "name"
    )
}
