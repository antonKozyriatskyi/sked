package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.Item

/**
 * Created by Anton on 06.07.2017.
 */
interface TeacherInfoProvider {
    suspend fun getDepartments(): List<Item>
    suspend fun getTeachers(departmentId: String): List<Item>
}