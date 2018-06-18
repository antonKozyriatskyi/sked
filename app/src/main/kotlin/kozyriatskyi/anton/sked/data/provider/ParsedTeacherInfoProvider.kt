package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.repository.TeacherInfoProvider
import kozyriatskyi.anton.sutparser.ParsedItem
import kozyriatskyi.anton.sutparser.TeacherInfoParser

/**
 * Created by Anton on 01.08.2017.
 */
class ParsedTeacherInfoProvider(private val parser: TeacherInfoParser) : TeacherInfoProvider {

    override fun getDepartments(): List<Item> = parser.getDepartments()
            .map(::toItem)

    override fun getTeachers(departmentId: String): List<Item> {
        return parser.getTeachers(departmentId)
                .map(::toItem)
    }

    private fun toItem(parsedItem: ParsedItem): Item = Item(parsedItem.id, parsedItem.value)
}