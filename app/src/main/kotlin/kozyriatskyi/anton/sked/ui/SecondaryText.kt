package kozyriatskyi.anton.sked.ui

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle

@Composable
fun SecondaryText(
    text: String,
    style: TextStyle = LocalTextStyle.current
) {
    CompositionLocalProvider(
        LocalContentAlpha provides ContentAlpha.medium
    ) {
        Text(
            text = text,
            style = style
        )
    }
}