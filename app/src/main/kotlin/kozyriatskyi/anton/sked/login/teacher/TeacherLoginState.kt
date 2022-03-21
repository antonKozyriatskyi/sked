package kozyriatskyi.anton.sked.login.teacher

import kozyriatskyi.anton.sked.data.pojo.Item

/**
 * Created by Anton on 07.07.2017.
 */
data class TeacherLoginState(
    val departments: List<Item>? = null,
    val selectedDepartment: Item? = null,

    val teachers: List<Item>? = null,
    val selectedTeacher: Item? = null,

    val error: TeacherLoginError? = null,

    val showLoading: Boolean = true,
    val enableUi: Boolean = true,
    val isLoaded: Boolean = false
)