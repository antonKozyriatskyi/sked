package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import java.time.LocalDate

/**
 * Created by Anton on 07.07.2017.
 */
interface StudentScheduleProvider {

    suspend fun getSchedule(
        facultyId: String,
        courseId: String,
        groupId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<LessonNetwork>
}

