package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.Item

/**
 * Created by Anton on 06.07.2017.
 */
interface StudentInfoLoader {
    fun getFaculties(): List<Item>
    fun getCourses(facultyId: String): List<Item>
    fun getGroups(courseId: String): List<Item>
}