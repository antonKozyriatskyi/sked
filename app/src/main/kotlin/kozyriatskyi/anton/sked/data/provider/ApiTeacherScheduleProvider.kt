package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.api.TeacherApi
import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.repository.TeacherScheduleProvider
import kozyriatskyi.anton.sked.util.DateFormatter
import java.time.LocalDate

/**
 * Created by Anton on 01.08.2017.
 */
class ApiTeacherScheduleProvider(
    private val api: TeacherApi,
    private val dateFormatter: DateFormatter
) : TeacherScheduleProvider {

    override suspend fun getSchedule(
        departmentId: String,
        teacherId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<LessonNetwork> {
        return api.getSchedule(
            departmentId = departmentId,
            teacherId = teacherId,
            dateStart = dateFormatter.long(startDate),
            dateEnd = dateFormatter.long(endDate)
        )
            .schedule
    }
}