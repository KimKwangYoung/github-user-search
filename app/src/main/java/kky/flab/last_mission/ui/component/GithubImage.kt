package kky.flab.last_mission.ui.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin

@Composable
fun GithubImage(
    imageSource: Any,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shape: GithubImageShape = GithubImageShape.Rectangle,
    error: Painter? = null,
    loading: Painter? = null,
) {
    CoilImage(
        imageModel = { imageSource },
        modifier = when(shape) {
            GithubImageShape.Circle -> modifier.clip(CircleShape)
            GithubImageShape.Rectangle -> modifier
            is GithubImageShape.Rounded -> modifier.clip(RoundedCornerShape(shape.radius))
        },
        component = rememberImageComponent {
            +PlaceholderPlugin.Failure(error)
            +PlaceholderPlugin.Loading(loading)
        },
        imageOptions = ImageOptions(
            alignment = Alignment.Center,
            contentScale = contentScale,
        )
    )
}



sealed interface GithubImageShape {
    data object Rectangle : GithubImageShape
    data object Circle : GithubImageShape
    data class Rounded(val radius: Int) : GithubImageShape
}