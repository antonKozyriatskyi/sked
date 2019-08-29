package kozyriatskyi.anton.sutparser

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
    */
    private val shortNamePattern: Pattern by lazy {
        Pattern.compile("<span>(.*)\\[(.*)\\]</span>\\s*<br>\\s*ауд\\. (.*)\\s*<br>\\s*(.*)\\s*")
    }
    private val shortNamePattern2: Pattern by lazy {
        Pattern.compile("<span>(.*)</span>\\s*<br>\\s*ауд\\. (.*)\\s*<br>\\s*(.*)\\s*")
    }

    /*
    <br>DevOps[Пз]<br>                     | <br>(.*)\[(.*)\]<br>\s*        - full name (1), type (2)
     ПДМ-51<br>                            | (.*)<br>\s*                    - group name (3)
    ауд. 302<br>                           | ауд\. (.*)<br>\s*              - cabinet (4)
    Онищенко Вікторія Валеріївна<br>       | (.*)<br>/s*                    - full teacher name (5)
    Добавлено: 19.08.2019 09:23<br>        | Добавлено: (.*)\s(.*)<br>\s*   - added on date (6), added on time (7)
    */
    private val allInfoPattern: Pattern by lazy {
        Pattern.compile("<br>(.*)\\[(.*)\\]<br>\\s*(.*)<br>\\s*ауд\\. (.*)<br>\\s*(.*)<br>\\s*Добавлено: (.*)\\s(.*)<br>\\s*")
    }

    // date in 'dd.MM.yyyy'
    fun getSchedule(facultyId: String, courseId: String, groupId: String, dateStart: String, dateEnd: String): List<ParsedLesson> {
        val table = doc(facultyId, courseId, groupId, dateStart, dateEnd)
                .getElementById("timeTableGroup")

        return parseSchedule(table)
    }

    private fun doc(facultyId: String, courseId: String, groupId: String, dateStart: String, dateEnd: String) =
            loadDocument(url(facultyId, courseId, groupId, dateStart, dateEnd))

    private fun url(facultyId: String, courseId: String, groupId: String, dateStart: String, dateEnd: String): String =
            "${BASE_URL}TimeTableForm[faculty]=$facultyId&TimeTableForm[course]=$courseId&TimeTableForm[group]=$groupId&TimeTableForm[date1]=$dateStart&TimeTableForm[date2]=$dateEnd&TimeTableForm[r11]=5&timeTable=0"

    private fun parseSchedule(table: Element): List<ParsedLesson> {
        val lessons = ArrayList<ParsedLesson>(100) // guess on average number of lessons
        val rows = table.getElementsByTag("tr")
        val notNumberPattern = Regex("\\D*")

        for (rowInd in rows.indices) {
            val row = rows[rowInd]
            val columns = row.getElementsByTag("td")
            val lessonNumbers = ArrayList<String>(4)

            for (columnInd in columns.indices) {
                val column = columns[columnInd]
                if (columnInd == 0) {
                    val divs = column.getElementsByClass("mh-50 cell cell-vertical")
                    divs.forEach {
                        val lessonNumber = it.getElementsByClass("lesson").text()
                                .replace(notNumberPattern, "")
                        lessonNumbers.add(lessonNumber)
                    }
                } else if (column.hasClass("closed").not()) {
                    val date = column.child(0).text()

                    val divInfoTexts = column.getElementsByClass("cell mh-50")

                    for (divInd in divInfoTexts.indices) {
                        val element = divInfoTexts[divInd]
                        if (element.childNodeSize() == 0) continue

                        val longLessonData = element.attr("data-content")

                        val text = element.child(0).html()
                        val generalMatcher = allInfoPattern.matcher(longLessonData)
                        var shortNameMatcher = shortNamePattern.matcher(text)

                        val number = lessonNumbers[divInd]
                        var shortName: String? = null
                        var name: String? = null
                        var type: String? = null
                        var cabinet: String? = null
                        var teacher: String? = null
                        var teacherShort: String? = null
                        var addedOnDate: String? = null
                        var addedOnTime: String? = null

                        if (generalMatcher.find()) {
                            name = generalMatcher.group(1)?.trim()
                            type = generalMatcher.group(2)?.trim()
                            cabinet = generalMatcher.group(4)?.trim()
                            teacher = generalMatcher.group(5)?.trim()
                            addedOnDate = generalMatcher.group(6)?.trim()
                            addedOnTime = generalMatcher.group(7)?.trim()
                        }

                        if (shortNameMatcher.find()) {
                            shortName = shortNameMatcher.group(1)?.trim()
//                            val type2 = shortNameMatcher.group(2)?.trim() // same as type
//                            val cabinet2 = shortNameMatcher.group(3)?.trim() // same as cabinet
                            teacherShort = shortNameMatcher.group(4)?.trim()
                        } else {
                            shortNameMatcher = shortNamePattern2.matcher(text)  // trying with fallback matcher
                            if (shortNameMatcher.find()) {
                                shortName = shortNameMatcher.group(1)?.trim()
                                teacherShort = shortNameMatcher.group(3)?.trim()
                            }
                        }

                        verifyAllPresentOrThrow("couldn't parse student's schedule",
                                shortName,
                                name,
                                type,
                                cabinet,
                                teacher,
                                teacherShort,
                                addedOnDate,
                                addedOnTime
                        )

                        // ugly force-unwraps but I couldn't find a better solution
                        val lesson = ParsedLesson(
                                date = date,
                                number = number,
                                shortName = shortName!!,
                                type = type!!,
                                cabinet = cabinet!!,
                                who = teacher!!,
                                name = name!!,
                                addedOnDate = addedOnDate!!,
                                addedOnTime = addedOnTime!!,
                                whoShort = teacherShort!!
                        )

                        lessons.add(lesson)
                    }
                }
            }
        }

        return lessons
    }
}