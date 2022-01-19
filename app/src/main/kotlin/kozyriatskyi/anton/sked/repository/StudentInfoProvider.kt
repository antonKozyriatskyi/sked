package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.Item

/**
 * Created by Anton on 06.07.2017.
 */
interface StudentInfoProvider {
    suspend fun getFaculties(): List<Item>
    suspend fun getCourses(facultyId: String): List<Item>
    suspend fun getGroups(facultyId: String, courseId: String): List<Item>
}