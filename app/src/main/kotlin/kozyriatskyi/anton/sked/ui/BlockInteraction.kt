package kozyriatskyi.anton.sked.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import kozyriatskyi.anton.sked.util.ComposableBody

@Composable
fun BlockInteraction(
    content: ComposableBody
) {
    Box(
        modifier = Modifier.pointerInput(key1 = Unit) {
            awaitPointerEventScope {
                while (true) {
                    awaitPointerEvent(pass = PointerEventPass.Initial)
                        .changes
                        .forEach(PointerInputChange::consumeAllChanges)
                }
            }
        },
        content = { content() },
    )
}