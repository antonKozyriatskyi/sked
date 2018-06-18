package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.User

interface ScheduleProvider {
    fun getSchedule(user: User): List<LessonNetwork>
}