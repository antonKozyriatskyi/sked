package kozyriatskyi.anton.sked.day

import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.data.pojo.LessonDb
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import java.time.LocalDate

class DayViewInteractor(private val scheduleStorage: ScheduleStorage,
                        private val userInfoStorage: UserInfoStorage) {

    fun observeLessons(date: LocalDate): Flow<List<LessonDb>> {
        return scheduleStorage.getLessonsOnDate(date)
    }

    fun getUser(): User = userInfoStorage.getUser()
}