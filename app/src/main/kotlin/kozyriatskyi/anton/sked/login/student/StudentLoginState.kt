package kozyriatskyi.anton.sked.login.student

import kozyriatskyi.anton.sked.data.pojo.Item

/**
 * Created by Anton on 07.07.2017.
 */
data class StudentLoginState(
    val faculties: List<Item>? = null,
    val selectedFaculty: Item? = null,

    val courses: List<Item>? = null,
    val selectedCourse: Item? = null,

    val groups: List<Item>? = null,
    val selectedGroup: Item? = null,

    val error: StudentLoginError? = null,

    val showLoading: Boolean = true,
    val enableUi: Boolean = true,
    val isLoaded: Boolean = false
)