package kozyriatskyi.anton.sked.data.parser

import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.util.DateUtils
import kozyriatskyi.anton.sked.util.logD
import org.jsoup.Jsoup

/**
 * Created by Anton on 01.08.2017.
 */
class StudentInfoParser {

    companion object {
        private const val BASE_URL = "http://e-rozklad.dut.edu.ua/timeTable/group?"
    }

    fun getFaculties(): List<Item> {

        val faculties = doc()
                .getElementById("TimeTableForm_faculty")
                .getElementsByAttributeValueMatching("value", "[0-9]+")
                .map(::Item)
                .toList()

        return faculties
    }

    fun getCourses(facultyId: String): List<Item> {

        val courses = doc(facultyId = facultyId)
                .getElementById("TimeTableForm_course")
                .getElementsByAttributeValueMatching("value", "[0-9]+")
                .map(::Item)
                .toList()

        return courses
    }

    fun getGroups(facultyId: String, courseId: String): List<Item> {

        val groups = doc(facultyId = facultyId, courseId = courseId)
                .getElementById("TimeTableForm_group")
                .getElementsByAttributeValueMatching("value", "[0-9]+")
                .map(::Item)
                .toList()

        return groups
    }

    private fun doc(facultyId: String = "", courseId: String = "")
            = Jsoup.connect(url(facultyId, courseId)).timeout(10_000).get()

    private fun url(facultyId: String = "", courseId: String = ""): String {
        val s = "${BASE_URL}TimeTableForm[faculty]=$facultyId&TimeTableForm[course]=$courseId&TimeTableForm[group]=&TimeTableForm[date1]=${DateUtils.mondayDate()}&TimeTableForm[date2]=${DateUtils.saturdayDate(5)}&TimeTableForm[r11]=5&timeTable=0"
        this.logD("Student URL: $s")
        return s
    }
}