package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.repository.TeacherScheduleProvider
import kozyriatskyi.anton.sked.util.DateFormatter
import kozyriatskyi.anton.sutparser.TeacherScheduleParser
import java.time.LocalDate

/**
 * Created by Anton on 01.08.2017.
 */
class ParsedTeacherScheduleProvider(
    private val parser: TeacherScheduleParser,
    private val dateFormatter: DateFormatter
) : TeacherScheduleProvider {

    override fun getSchedule(
        departmentId: String,
        teacherId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<LessonNetwork> {
        return parser.getSchedule(
            departmentId = departmentId,
            teacherId = teacherId,
            dateStart = dateFormatter.long(startDate),
            dateEnd = dateFormatter.long(endDate)
        )
            .map {
                LessonNetwork(
                    dateFormatter.parse(it.date),
                    it.number,
                    it.type,
                    it.cabinet,
                    it.shortName,
                    it.name,
                    it.addedOnDate,
                    it.addedOnTime,
                    it.who,
                    it.whoShort
                )
            }
    }
}