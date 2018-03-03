package kozyriatskyi.anton.sked.day

import io.reactivex.Observable
import kozyriatskyi.anton.sked.data.pojo.Day
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateUtils

class DayViewInteractor(private val scheduleStorage: ScheduleStorage,
                        private val userInfoStorage: UserInfoStorage) {

    fun lessons(dayNumber: Int, weekNum: Int): Observable<Day> {
        val date = DateUtils.longDateForDayNum(dayNumber, weekNum)
        return scheduleStorage.getLessonsByDate(dayNumber, weekNum, date)
    }

    fun getUser(): User = userInfoStorage.getUser()
}