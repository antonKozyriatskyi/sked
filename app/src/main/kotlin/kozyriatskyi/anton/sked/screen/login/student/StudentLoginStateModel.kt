package kozyriatskyi.anton.sked.screen.login.student

import androidx.compose.runtime.Immutable
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.screen.login.student.StudentLoadingError

@Immutable
data class StudentLoginStateModel(
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false,
    val error: StudentLoadingError? = null,
    val isConnectionAvailable: Boolean = true,

    val faculties: List<Item>? = null,
    val courses: List<Item>? = null,
    val groups: List<Item>? = null,

    val selectedFaculty: Item? = null,
    val selectedCourse: Item? = null,
    val selectedGroup: Item? = null,

    val scheduleLoaded: Boolean = false
) {

    val isError: Boolean get() = error != null

    val enableUi: Boolean
        get() = isConnectionAvailable and isLoading.not() and isError.not()

    val showProgress: Boolean
        get() = isConnectionAvailable and isLoading and isError.not() and isLoaded.not()
}