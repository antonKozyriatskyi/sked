package kozyriatskyi.anton.sked.data.repository

import kozyriatskyi.anton.sked.data.parser.StudentInfoParser
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.repository.StudentInfoLoader

class StudentParsedInfoLoader(private val parser: StudentInfoParser) : StudentInfoLoader {

    private var facultyId = ""

    override fun getFaculties(): List<Item> {
        facultyId = ""

        return parser.getFaculties()
    }

    override fun getCourses(facultyId: String): List<Item> {
        this.facultyId = facultyId

        return parser.getCourses(facultyId)
    }

    override fun getGroups(courseId: String): List<Item> {
        return parser.getGroups(facultyId, courseId)
    }
}