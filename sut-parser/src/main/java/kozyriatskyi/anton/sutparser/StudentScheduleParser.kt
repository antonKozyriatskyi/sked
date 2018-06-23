package kozyriatskyi.anton.sutparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Anton on 01.08.2017.
 */
class StudentScheduleParser {

    companion object {
        private const val BASE_URL = "http://e-rozklad.dut.edu.ua/timeTable/group?"
    }

    /*
    <span>МП ПЗ-2017[Пз]</span><br> | <span>(.*)\[?(.*)\]?</span>\s*<br>\s* - short name, type | sometimes there's no square brackets
    ауд. 310<br>                    | ауд\. (.*)\s*<br>\s*                  - cabinet
    Гаманюк І.М.                    | (.*)\s*                               - short teacher
    <span class="hidden"></span>    | <span class="hidden"></span>\s*
    */
    private val shortNamePattern: Pattern by lazy {
        Pattern.compile("<span>(.*)\\[(.*)\\]</span>\\s*<br>\\s*ауд\\. (.*)\\s*<br>\\s*(.*)\\s*<span class=\"hidden\"></span>\\s*")
    }
    private val shortNamePattern2: Pattern by lazy {
        Pattern.compile("<span>(.*)</span>\\s*<br>\\s*ауд\\. (.*)\\s*<br>\\s*(.*)\\s*<span class=\"hidden\"></span>\\s*")
    }

    /*
    Моделювання та проектування ПЗ[Пз]<br> | (.*)\[(.*)\]<br>\s*            - full name, type
    <br>                                   | <br>\s*
    ауд. 310<br>                           | ауд\. (.*)<br>\s*              - cabinet
    Гаманюк Ігор Михайлович<br>            | (.*)<br>                       - full teacher
    Добавлено: 13.12.2017 12:09<br>        | Добавлено: (.*)\s(.*)<br>\s*   - added on date, added on time
    <a href='#'>Доп. материалы</a          | <a href='#'>Доп\. материалы</a
    */
    private val allInfoPattern: Pattern by lazy {
        Pattern.compile("(.*)\\[(.*)\\]<br>\\s*<br>\\s*ауд\\. (.*)<br>\\s*(.*)<br>\\s*Добавлено: (.*)\\s(.*)<br>\\s*<a href='#'>Доп\\. материалы</a")
    }

    // date in 'dd.MM.yyyy'
    fun getSchedule(facultyId: String, courseId: String, groupId: String, dateStart: String, dateEnd: String): List<ParsedLesson> {
        val table = doc(facultyId, courseId, groupId, dateStart, dateEnd)
                .getElementById("timeTableGroup")

        return parseSchedule(table)
    }

    private fun doc(facultyId: String, courseId: String, groupId: String, dateStart: String, dateEnd: String) =
            Jsoup.connect(url(facultyId, courseId, groupId, dateStart, dateEnd)).timeout(TIMEOUT).get()

    private fun url(facultyId: String, courseId: String, groupId: String, dateStart: String, dateEnd: String): String {
        @Suppress("UnnecessaryVariable")
        val url = "${BASE_URL}TimeTableForm[faculty]=$facultyId&TimeTableForm[course]=$courseId&TimeTableForm[group]=$groupId&TimeTableForm[date1]=$dateStart&TimeTableForm[date2]=$dateEnd&TimeTableForm[r11]=5&timeTable=0"
        return url
    }

    private fun parseSchedule(table: Element): List<ParsedLesson> {
        val lessons = ArrayList<ParsedLesson>(80) // guess on average number of lessons
        val rows = table.getElementsByTag("tr")
        val notNumberPattern = Regex("\\D*")

        for (row_i in rows.indices) {
            val row = rows[row_i]
            val columns = row.getElementsByTag("td")
            val lessonNumbers = ArrayList<String>(4)
            for (column_i in columns.indices) {
                if (column_i == 0) {
                    val divs = columns[column_i].getElementsByClass("mh-50 cell cell-vertical")
                    divs.forEach {
                        val lessonNumber = it.getElementsByClass("lesson").text()
                                .replace(notNumberPattern, "")
                        lessonNumbers.add(lessonNumber)
                    }
                } else if (columns[column_i].hasClass("closed").not()) {
                    val column = columns[column_i]
                    val date = column.child(0).text()

                    val divInfoTexts = column.getElementsByClass("cell mh-50")

                    for (div_ind in divInfoTexts.indices) {
                        val element = divInfoTexts[div_ind]
                        if (element.childNodeSize() == 0) continue

                        val longLessonData = element.attr("data-content")

                        val text = element.child(0).html()
                        val generalMatcher = allInfoPattern.matcher(longLessonData)
                        var shortNameMatcher = shortNamePattern.matcher(text)

                        val number = lessonNumbers[div_ind]
                        var shortName = ""
                        var name = ""
                        var type = ""
                        var cabinet = ""
                        var teacher = ""
                        var teacherShort = ""
                        var addedOnDate = ""
                        var addedOnTime = ""

                        if (generalMatcher.find()) {
                            name = generalMatcher.group(1).trim()
                            type = generalMatcher.group(2).trim()
                            cabinet = generalMatcher.group(3).trim()
                            teacher = generalMatcher.group(4).trim()
                            addedOnDate = generalMatcher.group(5).trim()
                            addedOnTime = generalMatcher.group(6).trim()
                        }

                        if (shortNameMatcher.find()) {
                            shortName = shortNameMatcher.group(1).trim()
//                            val type2 = shortNameMatcher.group(2).trim() // same as type
//                            val cabinet2 = shortNameMatcher.group(3).trim() // same as cabinet
                            teacherShort = shortNameMatcher.group(4).trim()
                        } else {
                            shortNameMatcher = shortNamePattern2.matcher(text)  // trying with fallback matcher
                            if (shortNameMatcher.find()) {
                                shortName = shortNameMatcher.group(1).trim()
                                teacherShort = shortNameMatcher.group(3).trim()
                            }
                        }

                        val lesson = ParsedLesson(date = date, number = number, shortName = shortName, type = type,
                                cabinet = cabinet, who = teacher, name = name, addedOnDate = addedOnDate,
                                addedOnTime = addedOnTime, whoShort = teacherShort)
                        lessons.add(lesson)
                    }
                }
            }
        }

        return lessons
    }
}