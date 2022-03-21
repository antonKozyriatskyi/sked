package kozyriatskyi.anton.sked.ui

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import kozyriatskyi.anton.sked.util.ComposableBody

@Composable
fun Interaction(
    enabled: Boolean,
    content: ComposableBody
) {
    CompositionLocalProvider(
        LocalContentAlpha provides if (enabled) {
            LocalContentAlpha.current
        } else {
            ContentAlpha.disabled
        }
    ) {
        if (enabled) {
            content()
        } else {
            BlockInteraction(content)
        }
    }
}