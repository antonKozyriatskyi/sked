package kozyriatskyi.anton.sutparser

import org.jsoup.Jsoup

/**
 * Created by Anton on 01.08.2017.
 */
class TeacherInfoParser {

    companion object {
        private const val BASE_URL = "http://e-rozklad.dut.edu.ua/timeTable/teacher?"
        private const val TIMEOUT = 10_000
    }

    fun getDepartments(): List<ParsedItem> {
        val departments = doc()
                .getElementById("TimeTableForm_chair")
                .getElementsByAttributeValueMatching("value", "[0-9]+")
                .map(::ParsedItem)
                .toList()

        return departments
    }

    fun getTeachers(departmentId: String): List<ParsedItem> {

        val teachers = doc(departmentId)
                .getElementById("TimeTableForm_teacher")
                .getElementsByAttributeValueMatching("value", "[0-9]+")
                .map(::ParsedItem)
                .toList()

        return teachers
    }

    private fun doc(departmentId: String = "") = Jsoup.connect(url(departmentId)).timeout(TIMEOUT).get()

    private fun url(departmentId: String): String {
//        val url = "${BASE_URL}TimeTableForm[chair]=$departmentId&TimeTableForm[teacher]=&TimeTableForm[date1]=${DateUtils.mondayDate()}&TimeTableForm[date2]=${DateUtils.saturdayDate(5)}&timeTable=0&TimeTableForm[r11]=5"
        @Suppress("UnnecessaryVariable")
        val url = "${BASE_URL}TimeTableForm[chair]=$departmentId&TimeTableForm[teacher]=" // no need to pass other parameters
        return url
    }
}