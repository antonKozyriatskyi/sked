package kozyriatskyi.anton.sked.util

import io.reactivex.Single


inline fun <T> safeSingle(crossinline dataProvider: () -> T): Single<T> = Single.create<T> {
    try {
        val data = dataProvider()
        if (it.isDisposed.not()) {
            it.onSuccess(data)
        }
    } catch (t: Throwable) {
        if (it.isDisposed.not()) {
            it.onError(t)
        }
    }
}