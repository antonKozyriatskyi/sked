package kozyriatskyi.anton.sked.week

import io.reactivex.Observable
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
        return Observable.mergeArray(
                scheduleStorage.getLessonsByDate(0, weekNumber, mondayDate),
                scheduleStorage.getLessonsByDate(1, weekNumber, tuesdayDate),
                scheduleStorage.getLessonsByDate(2, weekNumber, wednesdayDate),
                scheduleStorage.getLessonsByDate(3, weekNumber, thursdayDate),
                scheduleStorage.getLessonsByDate(4, weekNumber, fridayDate),
                scheduleStorage.getLessonsByDate(5, weekNumber, saturdayDate),
                scheduleStorage.getLessonsByDate(6, weekNumber, sundayDate)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .buffer(7)
                .map { it.sortedBy { it.dayNumber } }
    }

    fun getUser(): User = userInfoStorage.getUser()
}