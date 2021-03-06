package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.Item

/**
 * Created by Anton on 06.07.2017.
 */
interface TeacherInfoProvider {
    fun getDepartments(): List<Item>
    fun getTeachers(departmentId: String): List<Item>
}