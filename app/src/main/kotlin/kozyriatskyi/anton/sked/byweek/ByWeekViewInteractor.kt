package kozyriatskyi.anton.sked.byweek

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateUtils

class ByWeekViewInteractor(private val scheduleStorage: ScheduleStorage) {

    fun firstWeekendLessonsCount(): Single<Pair<Boolean, Boolean>> {
        val scheduleRxDatabase = scheduleStorage

        val thisSat = scheduleRxDatabase.amountOfLessonsOnDate(DateUtils.saturdayDate())
        val thisSun = scheduleRxDatabase.amountOfLessonsOnDate(DateUtils.sundayDate())

        return Observable.zip(thisSat, thisSun, BiFunction { t1: Int, t2: Int -> Pair(t1, t2) })
                .map { Pair(it.first != 0, it.second != 0) }
                .take(1)
                .singleOrError()
    }
}