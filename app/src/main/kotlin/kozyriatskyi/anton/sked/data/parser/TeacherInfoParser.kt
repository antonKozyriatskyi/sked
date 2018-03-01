package kozyriatskyi.anton.sked.data.parser

import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.util.DateUtils
import kozyriatskyi.anton.sked.util.logD
import org.jsoup.Jsoup

/**
 * Created by Anton on 01.08.2017.
 */
class TeacherInfoParser {

    companion object {
        private const val BASE_URL = "http://e-rozklad.dut.edu.ua/timeTable/teacher?"
    }

    fun getDepartments(): List<Item> {
        val departments = doc()
                .getElementById("TimeTableForm_chair")
                .getElementsByAttributeValueMatching("value", "[0-9]+")
                .map(::Item)
                .toList()

        return departments
    }

    fun getTeachers(departmentId: String): List<Item> {

        val teachers = doc(departmentId)
                .getElementById("TimeTableForm_teacher")
                .getElementsByAttributeValueMatching("value", "[0-9]+")
                .map(::Item)
                .toList()

        return teachers
    }

    private fun doc(departmentId: String = "") = Jsoup.connect(url(departmentId)).timeout(10_000).get()

    private fun url(departmentId: String): String {
        val url = "${BASE_URL}TimeTableForm[chair]=$departmentId&TimeTableForm[teacher]=&TimeTableForm[date1]=${DateUtils.mondayDate()}&TimeTableForm[date2]=${DateUtils.saturdayDate(5)}&timeTable=0&TimeTableForm[r11]=5"
        this.logD("Teacher URL: $url")
        return url
    }
}