package kozyriatskyi.anton.sked.week

import io.reactivex.Observable
import io.reactivex.functions.Function7
import io.reactivex.schedulers.Schedulers
import kozyriatskyi.anton.sked.data.pojo.Day
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateUtils

class WeekViewInteractor(private val scheduleStorage: ScheduleStorage,
                         private val userInfoStorage: UserInfoStorage) {

    fun lessons(weekNumber: Int): Observable<List<Day>> {

        val mondayDate = DateUtils.mondayDate(weekNumber)
        val tuesdayDate = DateUtils.tuesdayDate(weekNumber)
        val wednesdayDate = DateUtils.wednesdayDate(weekNumber)
        val thursdayDate = DateUtils.thursdayDate(weekNumber)
        val fridayDate = DateUtils.fridayDate(weekNumber)
        val saturdayDate = DateUtils.saturdayDate(weekNumber)
        val sundayDate = DateUtils.sundayDate(weekNumber)

        fun observeDay(dayNumber: Int, date: String): Observable<Day> =
                scheduleStorage.getLessonsByDate(date)
                        .map { Day(dayNumber, weekNumber, date, it) }
                        .subscribeOn(Schedulers.io())

        return Observable.zip<Day, Day, Day, Day, Day, Day, Day, List<Day>>(
                observeDay(0, mondayDate),
                observeDay(1, tuesdayDate),
                observeDay(2, wednesdayDate),
                observeDay(3, thursdayDate),
                observeDay(4, fridayDate),
                observeDay(5, saturdayDate),
                observeDay(6, sundayDate),
                Function7 { d1, d2, d3, d4, d5, d6, d7 -> listOf(d1, d2, d3, d4, d5, d6, d7) }
        )
    }

    fun getUser(): User = userInfoStorage.getUser()
}