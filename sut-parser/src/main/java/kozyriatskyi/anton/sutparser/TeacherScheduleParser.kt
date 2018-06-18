package kozyriatskyi.anton.sutparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Anton on 01.08.2017.
 */
class TeacherScheduleParser {

    companion object {
        private const val BASE_URL = "http://e-rozklad.dut.edu.ua/timeTable/teacher?"
        private const val TIMEOUT = 10_000
    }

    /*
    <span>МММОП[Лк]</span> | <span>(.*)\[?(.*)\]?</span>\s* - short name, type | sometimes there's no square brackets
    <br> УБДМ-51           | <br>\s*(.*)\s*                 - group
    <br> ауд. 517          | <br>\s*ауд\. (.*)\s*           - cabinet
    */
    private val shortNamePattern: Pattern by lazy {
        Pattern.compile("<span>(.*)\\s*\\[(.*)\\]</span>\\s*<br>\\s*(.*)\\s*<br>\\s*ауд\\. (.*)\\s*")
    }

    private val shortNamePattern2: Pattern by lazy {
        Pattern.compile("<span>(.*)\\s*</span>\\s*<br>\\s*(.*)\\s*<br>\\s*ауд\\. (.*)\\s*")
    }

    /*
    Вища математика[Пз]<br>      | (.*)\[(.*)\]<br>\s*            - full name, type
    СЗД-11<br>                   | (.*)<br>\s*                    - group
    ауд. 517<br>                 | ауд\. (.*)<br>\s*              - cabinet
    Добавлено: 29.01.2018 16:09  | Добавлено: (.*)\s(.*)<br>\s*   - added on date, added on time
    */
    private val generalPattern: Pattern by lazy {
        Pattern.compile("(.*)\\[(.*)\\]<br>\\s*(.*)<br>\\s*ауд\\. (.*)<br>\\s*Добавлено: (.*)\\s(.*)\\s*")
    }

    // date in 'dd.MM.yyyy'
    fun getSchedule(departmentId: String, teacherId: String, dateStart: String, dateEnd: String): List<ParsedLesson> {
        val table = doc(departmentId, teacherId, dateStart, dateEnd)
                .getElementById("timeTableGroup")

        return parseSchedule(table)
    }

    private fun doc(departmentId: String, teacherId: String, dateStart: String, dateEnd: String) =
            Jsoup.connect(url(departmentId, teacherId, dateStart, dateEnd)).timeout(TIMEOUT).get()

    private fun url(departmentId: String, teacherId: String, dateStart: String, dateEnd: String): String {
        @Suppress("UnnecessaryVariable")
        val url = "${BASE_URL}TimeTableForm[chair]=$departmentId&TimeTableForm[teacher]=$teacherId&TimeTableForm[date1]=$dateStart&TimeTableForm[date2]=$dateEnd&timeTable=0&TimeTableForm[r11]=5"
        return url
    }

    private fun parseSchedule(table: Element): List<ParsedLesson> {
        val lessons = ArrayList<ParsedLesson>()
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
                        val generalMatcher = generalPattern.matcher(longLessonData)
                        var shortNameMatcher = shortNamePattern.matcher(text)

                        val number = lessonNumbers[div_ind]
                        var shortName = ""
                        var name = ""
                        var type = ""
                        var cabinet = ""
                        var group = ""
                        var addedOnDate = ""
                        var addedOnTime = ""

                        if (generalMatcher.find()) {
                            name = generalMatcher.group(1).trim()
                            type = generalMatcher.group(2).trim()
                            group = generalMatcher.group(3).trim()
                            cabinet = generalMatcher.group(4).trim()
                            addedOnDate = generalMatcher.group(5).trim()
                            addedOnTime = generalMatcher.group(6).trim()
                        }

                        if (shortNameMatcher.find()) {
                            shortName = shortNameMatcher.group(1).trim()
//                            val type2 = shortNameMatcher.group(2).trim() // same as type
//                            val group2 = shortNameMatcher.group(3).trim() // same as group
//                            val cabinet2 = shortNameMatcher.group(4).trim() // same as cabinet
                        } else {
                            shortNameMatcher = shortNamePattern2.matcher(text)
                            if (shortNameMatcher.find()) {
                                shortName = shortNameMatcher.group(1).trim()
                            }
                        }

                        val lesson = ParsedLesson(date = date, number = number, shortName = shortName,
                                type = type, cabinet = cabinet, who = group, name = name,
                                addedOnDate = addedOnDate, addedOnTime = addedOnTime, whoShort = group)
                        lessons.add(lesson)
                    }
                }
            }
        }

        return lessons
    }
}