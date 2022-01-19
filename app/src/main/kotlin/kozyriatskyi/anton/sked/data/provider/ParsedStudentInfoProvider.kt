package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.repository.StudentInfoProvider
import kozyriatskyi.anton.sutparser.ParsedItem
import kozyriatskyi.anton.sutparser.StudentInfoParser

/**
 * Created by Anton on 01.08.2017.
 */
class ParsedStudentInfoProvider(private val parser: StudentInfoParser) : StudentInfoProvider {

    override suspend fun getFaculties(): List<Item> {
        return parser.getFaculties()
                .map(::toItem)
    }

    override suspend fun getCourses(facultyId: String): List<Item> {
        return parser.getCourses(facultyId)
                .map(::toItem)
    }

    override suspend fun getGroups(facultyId: String, courseId: String): List<Item> {
        return parser.getGroups(facultyId = facultyId, courseId = courseId)
                .map(::toItem)
    }

    private fun toItem(parsedItem: ParsedItem): Item = Item(parsedItem.id, parsedItem.value)
}