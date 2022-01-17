package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.api.TeacherApi
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.LoginItem
import kozyriatskyi.anton.sked.repository.TeacherInfoProvider

/**
 * Created by Anton on 01.08.2017.
 */
class ApiTeacherInfoProvider(private val api: TeacherApi) : TeacherInfoProvider {

    override fun getDepartments(): List<Item> = api.getDepartments()
        .execute()
        .body()!!
        .map(::toItem)

    override fun getTeachers(departmentId: String): List<Item> = api.getTeachers(departmentId)
        .execute()
        .body()!!
        .map(::toItem)

    private fun toItem(item: LoginItem): Item = Item(item.id, item.value)
}