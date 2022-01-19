package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.api.TeacherApi
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.LoginItem
import kozyriatskyi.anton.sked.repository.TeacherInfoProvider

/**
 * Created by Anton on 01.08.2017.
 */
class ApiTeacherInfoProvider(private val api: TeacherApi) : TeacherInfoProvider {

    override suspend fun getDepartments(): List<Item> = api.getDepartments()
        .map(::toItem)

    override suspend fun getTeachers(departmentId: String): List<Item> = api.getTeachers(departmentId)
        .map(::toItem)

    private fun toItem(item: LoginItem): Item = Item(item.id, item.value)
}