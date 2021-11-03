package kozyriatskyi.anton.sked.week

import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.data.pojo.LessonDb
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import java.time.LocalDate

class WeekViewInteractor(
    private val scheduleStorage: ScheduleStorage,
    private val userInfoStorage: UserInfoStorage
) {

    fun getLessons(startDate: LocalDate, endDate: LocalDate): Flow<List<LessonDb>> {
        return scheduleStorage.getLessonsBetweenDates(startDate, endDate)
    }

    fun getUser(): User = userInfoStorage.getUser()
}