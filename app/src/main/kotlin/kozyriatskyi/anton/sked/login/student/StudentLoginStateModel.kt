package kozyriatskyi.anton.sked.login.student

/**
 * Created by Anton on 08.07.2017.
 */
class StudentLoginStateModel {

    var facultyPosition = 0
    var coursePosition = 0
    var groupPosition = 0

    var isConnectionAvailable: Boolean = false

    var isLoading: Boolean = false
        set(value) {
            if (value) {
                isLoaded = false
                isError = false
            }
            field = value
        }

    var isError: Boolean = false
        set(value) {
            if (value) {
                isLoading = false
                isLoaded = false
            }
            field = value
        }

    var isLoaded = false
        set(value) {
            if (value) {
                isError = false
                isLoading = false
            }
            field = value
        }

    val enableUi: Boolean
        get() = isConnectionAvailable and isLoading.not() and isError.not()

    val showProgress: Boolean
        get() = isConnectionAvailable and isLoading and isError.not() and isLoaded.not()
}