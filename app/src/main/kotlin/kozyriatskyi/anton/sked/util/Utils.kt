package kozyriatskyi.anton.sked.util

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Flow
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

typealias UnitCallback = () -> Unit

typealias ValueCallback<T> = (T) -> Unit

fun takeIf(condition: Boolean, producer: @Composable () -> Unit): @Composable (() -> Unit)? {
    return if (condition) producer else null
}

@SuppressLint("ComposableNaming")
@Composable
inline fun <T> rememberMutableState(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = rememberMutableState(calculation = { value }, policy = policy)

@Composable
inline fun <T> rememberMutableState(
    calculation: @DisallowComposableCalls () -> T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = remember {
    mutableStateOf(calculation(), policy)
}

@Composable
inline fun Int.asStringRes(): String = stringResource(this)