package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.api.StudentApi
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.LoginItem
import kozyriatskyi.anton.sked.repository.StudentInfoProvider

class ApiStudentInfoProvider(private val api: StudentApi) : StudentInfoProvider {

    override fun getFaculties(): List<Item> {
        return api.getFaculties()
            .execute()
            .body()!!
            .map(::toItem)
    }

    override fun getCourses(facultyId: String): List<Item> {
        return api.getCourses(facultyId)
            .execute()
            .body()!!
            .map(::toItem)
    }

    override fun getGroups(facultyId: String, courseId: String): List<Item> {
        return api.getGroups(facultyId = facultyId, courseId = courseId)
            .execute()
            .body()!!
            .map(::toItem)
    }

    private fun toItem(item: LoginItem): Item = Item(item.id, item.value)
}