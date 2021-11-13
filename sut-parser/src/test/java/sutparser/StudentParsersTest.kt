package sutparser

import kozyriatskyi.anton.sutparser.StudentInfoParser
import kozyriatskyi.anton.sutparser.StudentScheduleParser
import org.junit.Ignore
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Backbase R&D B.V. on 10.10.2021.
 */
class StudentParsersTest {

    private val infoParser = StudentInfoParser()
    private val scheduleParser = StudentScheduleParser()

    @Ignore
    @Test
    fun test() {
        val faculties = infoParser.getFaculties()

        for (faculty in faculties) {
            print(faculty.value, 0)

            val facultyId = faculty.id
            val courses = infoParser.getCourses(facultyId)

            for (course in courses) {
                print(course.value, 1)

                val courseId = course.id
                val groups = infoParser.getGroups(facultyId, courseId)

                for (group in groups) {
                    val groupId = group.id

                    val startDate = Calendar.getInstance().time
                    val endDate = Calendar.getInstance().apply {
                        add(Calendar.MONTH, 1)
                    }.time

                    val dateFormatter = SimpleDateFormat("dd.MM.yyyy")

                    val schedule = scheduleParser.getSchedule(
                        facultyId, courseId, groupId,
                        dateStart = dateFormatter.format(startDate),
                        dateEnd = dateFormatter.format(endDate)
                    )

                    var sectionStarted = false

                    for (lesson in schedule) {

                        val fields = setOf(
                            lesson.date,
                            lesson.number,
                            lesson.type,
                            lesson.cabinet,
                            lesson.shortName,
                            lesson.name,
                            lesson.addedOnDate,
                            lesson.addedOnTime,
                            lesson.who,
                            lesson.whoShort
                        )

                        if (fields.any(String::isEmpty)) {
                            if (sectionStarted.not()) {
                                sectionStarted = true
                                print(group.value, 2)
                            }

                            print("""
                            date: ${lesson.date}
                            number: ${lesson.number}
                            type: ${lesson.type}
                            cabinet: ${lesson.cabinet}
                            shortName: ${lesson.shortName}
                            name: ${lesson.name}
                            addedOnDate: ${lesson.addedOnDate}
                            addedOnTime: ${lesson.addedOnTime}
                            who: ${lesson.who}
                            whoShort: ${lesson.whoShort}
                            """.trimIndent(),
                                3
                            )
                        }
                    }
                }
            }
        }
    }

    private fun print(text: String, level: Int) {
        if (level == 0) {
            println(text)
        } else {
            println("${" ".repeat(level)}$text")
        }
    }
}