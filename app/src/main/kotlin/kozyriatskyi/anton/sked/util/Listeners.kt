package kozyriatskyi.anton.sked.util

/**
 * Created by Anton on 06.07.2017.
 */

interface OnInternetConnectionChangeListener {
    fun onInternetConnectionStateChanged(isAvailable: Boolean)
}

interface OnLoadingStateChangeListener {
    fun onLoadingStateChanged(isLoaded: Boolean)
}