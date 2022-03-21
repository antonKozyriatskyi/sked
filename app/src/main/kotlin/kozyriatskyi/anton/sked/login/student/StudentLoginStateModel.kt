package kozyriatskyi.anton.sked.login.student

import kotlin.properties.Delegates

/**
 * Created by Anton on 08.07.2017.
 */
class StudentLoginStateModel {

    var isConnectionAvailable: Boolean = false

    var isLoading: Boolean by Delegates.observable(false) { _, _, isLoading ->
        if (isLoading) {
            isLoaded = false
            isError = false
        }
    }

    var isError: Boolean by Delegates.observable(false) { _, _, isError ->
        if (isError) {
            isLoading = false
            isLoaded = false
        }
    }

    var isLoaded by Delegates.observable(false) { _, _, isLoaded ->
        if (isLoaded) {
            isError = false
            isLoading = false
        }
    }

    val enableUi: Boolean
        get() = isConnectionAvailable && isLoading.not() && isError.not()

    val showProgress: Boolean
        get() = isConnectionAvailable && isLoading && isError.not() && isLoaded.not()
}