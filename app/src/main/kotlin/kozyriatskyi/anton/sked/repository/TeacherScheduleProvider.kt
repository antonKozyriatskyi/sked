package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import java.time.LocalDate

interface TeacherScheduleProvider {

    suspend fun getSchedule(
        departmentId: String,
        teacherId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<LessonNetwork>
}