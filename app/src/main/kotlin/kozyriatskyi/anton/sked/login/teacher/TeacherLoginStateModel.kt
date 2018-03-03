package kozyriatskyi.anton.sked.login.teacher

class TeacherLoginStateModel {

    var departmentPosition = 0
    var teacherPosition = 0

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