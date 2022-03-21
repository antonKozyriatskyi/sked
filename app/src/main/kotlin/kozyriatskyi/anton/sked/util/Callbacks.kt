package kozyriatskyi.anton.sked.util

import androidx.compose.runtime.Composable

/**
 * Created by Backbase R&D B.V. on 20.03.2022.
 */
typealias Callback = () -> Unit

typealias ValueCallback<T> = (T) -> Unit

typealias ComposableBody = @Composable () -> Unit
