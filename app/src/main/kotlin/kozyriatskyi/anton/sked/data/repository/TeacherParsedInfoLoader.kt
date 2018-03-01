package kozyriatskyi.anton.sked.data.repository

import kozyriatskyi.anton.sked.data.parser.TeacherInfoParser
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.domain.repository.TeacherInfoLoader

class TeacherParsedInfoLoader(private val parser: TeacherInfoParser) : TeacherInfoLoader {

    private var departmentId = ""

    override fun getDepartments(): List<Item> {
        // [departmentId] should be empty when requesting departments
        departmentId = ""

        return parser.getDepartments()
    }

    override fun getTeachers(departmentId: String): List<Item> {
        this.departmentId = departmentId

        return parser.getTeachers(departmentId)
    }
}