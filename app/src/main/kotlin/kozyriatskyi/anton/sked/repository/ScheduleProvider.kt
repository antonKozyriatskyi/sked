package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.User
import java.time.LocalDate

interface ScheduleProvider {

    fun getSchedule(user: User, startDate: LocalDate, endDate: LocalDate): List<LessonNetwork>
}