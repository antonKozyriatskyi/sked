package kozyriatskyi.anton.sked.util

import android.annotation.SuppressLint
import androidx.compose.runtime.*


/**
 * Created by Backbase R&D B.V. on 19.03.2022.
 */
@SuppressLint("ComposableNaming")
@Composable
inline fun <T> rememberMutableState(
    calculation: () -> T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = rememberMutableState(value = calculation(), policy = policy)

@Suppress("NOTHING_TO_INLINE")
@SuppressLint("ComposableNaming")
@Composable
inline fun <T> rememberMutableState(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = remember { mutableStateOf(value, policy) }
