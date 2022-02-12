package kozyriatskyi.anton.sked.util

import androidx.compose.runtime.Composable

fun takeIf(condition: Boolean, producer: @Composable () -> Unit): @Composable (() -> Unit)? {
    return if (condition) producer else null
}