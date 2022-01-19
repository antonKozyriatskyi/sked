package kozyriatskyi.anton.sked.data.provider

import kozyriatskyi.anton.sked.data.api.StudentApi
import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.repository.StudentScheduleProvider
import kozyriatskyi.anton.sked.util.DateFormatter
import java.time.LocalDate

class ApiStudentScheduleProvider(
    private val api: StudentApi,
    private val dateFormatter: DateFormatter
) : StudentScheduleProvider {

    override suspend fun getSchedule(
        facultyId: String,
        courseId: String,
        groupId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<LessonNetwork> = api.getSchedule(
        facultyId = facultyId,
        courseId = courseId,
        groupId = groupId,
        dateStart = dateFormatter.long(startDate),
        dateEnd = dateFormatter.long(endDate)
    ).schedule
}