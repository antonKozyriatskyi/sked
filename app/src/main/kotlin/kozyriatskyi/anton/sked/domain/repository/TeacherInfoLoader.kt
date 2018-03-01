package kozyriatskyi.anton.sked.domain.repository

import kozyriatskyi.anton.sked.data.pojo.Item

/**
 * Created by Anton on 06.07.2017.
 */
interface TeacherInfoLoader {
    fun getDepartments(): List<Item>
    fun getTeachers(departmentId: String): List<Item>
}