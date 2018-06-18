package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.repository.TeacherScheduleProvider
import kozyriatskyi.anton.sked.util.DateUtils
import kozyriatskyi.anton.sutparser.TeacherScheduleParser

/**
 * Created by Anton on 01.08.2017.
 */

class ParsedTeacherScheduleProvider(private val parser: TeacherScheduleParser) : TeacherScheduleProvider {

    override fun getSchedule(departmentId: String, teacherId: String): List<LessonNetwork> {
        return parser.getSchedule(departmentId, teacherId, DateUtils.mondayDate(), DateUtils.sundayDate(5))
                .map {
                    LessonNetwork(
                            it.date,
                            it.number,
                            it.type,
                            it.cabinet,
                            it.shortName,
                            it.name,
                            it.addedOnDate,
                            it.addedOnTime,
                            it.who,
                            it.whoShort)
                }
    }
}