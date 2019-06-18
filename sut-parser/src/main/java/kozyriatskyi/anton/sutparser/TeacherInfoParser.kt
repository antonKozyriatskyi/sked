package kozyriatskyi.anton.sutparser

/**
 * Created by Anton on 01.08.2017.
 */
class TeacherInfoParser {

    companion object {
        private const val BASE_URL = "http://e-rozklad.dut.edu.ua/timeTable/teacher?"
    }

    fun getDepartments(): List<ParsedItem> = doc().getElements("TimeTableForm_chair")

    fun getTeachers(departmentId: String): List<ParsedItem> =
            doc(departmentId).getElements("TimeTableForm_teacher")

    private fun doc(departmentId: String = "") = loadDocument(url(departmentId))

    private fun url(departmentId: String) = "${BASE_URL}TimeTableForm[chair]=$departmentId&TimeTableForm[teacher]="
}