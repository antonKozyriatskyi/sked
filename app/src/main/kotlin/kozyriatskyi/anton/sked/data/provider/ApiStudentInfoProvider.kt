package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.api.StudentApi
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.LoginItem
import kozyriatskyi.anton.sked.repository.StudentInfoProvider

class ApiStudentInfoProvider(private val api: StudentApi) : StudentInfoProvider {

    override suspend fun getFaculties(): List<Item> = api.getFaculties().map(::toItem)

    override suspend fun getCourses(facultyId: String): List<Item> {
        return api.getCourses(facultyId).map(::toItem)
    }

    override suspend fun getGroups(facultyId: String, courseId: String): List<Item> {
        return api.getGroups(facultyId = facultyId, courseId = courseId).map(::toItem)
    }

    private fun toItem(item: LoginItem): Item = Item(item.id, item.value)
}