@file:Suppress("UNCHECKED_CAST")

package kozyriatskyi.anton.sked.util

import kotlinx.coroutines.flow.*

fun <T> Flow<T>.onFirstEmit(action: suspend (T) -> Unit): Flow<T> = flow {
    var hadFirstEmit = false

    collect { value ->

        if (hadFirstEmit.not()) {
            hadFirstEmit = true
            action(value)
        }

        emit(value)
    }
}

fun <T> Flow<T>.batched(capacity: Int): Flow<List<T>> = flow {
    val items = ArrayList<T>(capacity)

    collect { value ->
        items.add(value)

        if (items.size == capacity) {
            emit(items.toList())
            items.clear()
        }
    }
}

fun <T1, T2, R> Flow<T1>.zipWith(
    other: Flow<T2>,
    transform: suspend (T1, T2) -> R
): Flow<R> = flow {
    collect { thisVal ->
        other.collect { thatVal ->
            emit(transform(thisVal, thatVal))
        }
    }
}

fun <T> List<Flow<T>>.zip(): Flow<List<T>> = zipFlows(
    flows = this,
    accumulator = ::emptyList,
    zipper = { acc, v -> acc + v }
)

fun <T, R> zipFlows(
    flows: List<Flow<T>>,
    accumulator: () -> R,
    zipper: (R, T) -> R,
): Flow<R> {

    check(flows.size > 1) {
        "Can't zip less than 1 flow"
    }

    var accF: Flow<R> = flowOf(accumulator())
    for (f in flows) {
        accF = accF.zip(f) { a, b -> zipper(a, b) }
    }

    return accF
}

fun <T> List<Flow<T>>.combine(): Flow<List<T>> = combineFlows(
    flows = this,
    accumulator = ::emptyList,
    combiner = { acc, v -> acc + v }
)


fun <T, R> combineFlows(
    flows: List<Flow<T>>,
    accumulator: () -> R,
    combiner: (R, T) -> R,
): Flow<R> {

    check(flows.size > 1) {
        "Can't combine less than 1 flow"
    }

    var accF: Flow<R> = flowOf(accumulator())
    for (f in flows) {
        accF = accF.combine(f) { a, b -> combiner(a, b) }
    }

    return accF
}
