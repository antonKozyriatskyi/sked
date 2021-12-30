package kozyriatskyi.anton.sked.screen.login.teacher

import androidx.compose.runtime.Immutable
import kozyriatskyi.anton.sked.data.pojo.Item

@Immutable
data class TeacherLoginStateModel(
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false,
    val error: TeacherLoadingError? = null,
    val isConnectionAvailable: Boolean = true,

    val departments: List<Item>? = null,
    val teachers: List<Item>? = null,

    val selectedDepartments: Item? = null,
    val selectedTeacher: Item? = null,

    val scheduleLoaded: Boolean = false
) {

    val isError: Boolean get() = error != null

    val enableUi: Boolean
        get() = isConnectionAvailable and isLoading.not() and isError.not()

    val showProgress: Boolean
        get() = isConnectionAvailable and isLoading and isError.not() and isLoaded.not()
}