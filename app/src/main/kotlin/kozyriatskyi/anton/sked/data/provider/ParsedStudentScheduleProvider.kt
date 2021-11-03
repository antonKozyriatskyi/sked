package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.repository.StudentScheduleProvider
import kozyriatskyi.anton.sked.util.DateFormatter
import kozyriatskyi.anton.sutparser.StudentScheduleParser
import java.time.LocalDate

/**
 * Created by Anton on 01.08.2017.
 */
class ParsedStudentScheduleProvider(
    private val parser: StudentScheduleParser,
    private val dateFormatter: DateFormatter
) : StudentScheduleProvider {

    override fun getSchedule(
        facultyId: String,
        courseId: String,
        groupId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<LessonNetwork> {
        return parser.getSchedule(
            facultyId = facultyId,
            courseId = courseId,
            groupId = groupId,
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