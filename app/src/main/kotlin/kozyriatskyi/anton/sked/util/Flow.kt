package kozyriatskyi.anton.sked.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

fun <T1, T2, T3, T4, T5, T6, T7, R> zipFlows(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> = flow1.zip(flow2) { a, b -> listOf(a, b) }
    .zip(flow3) { accumulator, value -> accumulator + value }
    .zip(flow4) { accumulator, value -> accumulator + value }
    .zip(flow5) { accumulator, value -> accumulator + value }
    .zip(flow6) { accumulator, value -> accumulator + value }
    .zip(flow7) { accumulator, value ->
        val list = accumulator + value

        transform(
            list[0] as T1,
            list[1] as T2,
            list[2] as T3,
            list[3] as T4,
            list[4] as T5,
            list[5] as T6,
            list[6] as T7,
        )
    }