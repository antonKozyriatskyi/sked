package kozyriatskyi.anton.sutparser

/**
 * Created by Anton on 01.08.2017.
 */
class StudentInfoParser {

    companion object {
        private const val BASE_URL = "http://e-rozklad.dut.edu.ua/timeTable/group?"
    }

    fun getFaculties(): List<ParsedItem> = doc().getElements("TimeTableForm_faculty")

    fun getCourses(facultyId: String): List<ParsedItem> =
            doc(facultyId = facultyId).getElements("TimeTableForm_course")

    fun getGroups(facultyId: String, courseId: String): List<ParsedItem> =
            doc(facultyId = facultyId, courseId = courseId).getElements("TimeTableForm_group")

    private fun doc(facultyId: String = "", courseId: String = "") = loadDocument(url(facultyId, courseId))

    private fun url(facultyId: String = "", courseId: String = "") =
            "${BASE_URL}TimeTableForm[faculty]=$facultyId&TimeTableForm[course]=$courseId&TimeTableForm[group]="
}