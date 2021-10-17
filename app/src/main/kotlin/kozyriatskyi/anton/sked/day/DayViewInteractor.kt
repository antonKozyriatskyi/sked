package kozyriatskyi.anton.sked.day

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kozyriatskyi.anton.sked.data.pojo.Day
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateUtils

class DayViewInteractor(private val scheduleStorage: ScheduleStorage,
                        private val userInfoStorage: UserInfoStorage) {

    fun lessons(dayNumber: Int, weekNum: Int): Flow<Day> {
        val date = DateUtils.longDateForDayNum(dayNumber, weekNum)
        return scheduleStorage.getLessonsByDate(date)
                .map { Day(dayNumber, weekNum, date, it) }
    }

    fun getUser(): User = userInfoStorage.getUser()
}