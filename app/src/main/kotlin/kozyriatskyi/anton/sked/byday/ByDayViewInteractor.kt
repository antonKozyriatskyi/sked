package kozyriatskyi.anton.sked.byday

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateUtils

class ByDayViewInteractor(private val scheduleStorage: ScheduleStorage) {

    fun thisWeekendLessonsCount(): Flow<Pair<Boolean, Boolean>> {
        val thisSat = scheduleStorage.amountOfLessonsOnDate(DateUtils.saturdayDate())
        val thisSun = scheduleStorage.amountOfLessonsOnDate(DateUtils.sundayDate())

        return thisSat.zip(thisSun) { t1: Int, t2: Int -> Pair(t1, t2) }
            .map { Pair(it.first != 0, it.second != 0) }
    }

    fun weekendLessonsCount(nextWeek: Boolean): Flow<Pair<Boolean, Boolean>> {
        val weekNum = if (nextWeek) 1 else 0

        val sat = scheduleStorage.amountOfLessonsOnDate(DateUtils.saturdayDate(weekNum))
        val sun = scheduleStorage.amountOfLessonsOnDate(DateUtils.sundayDate(weekNum))

        return sat.zip(sun) { t1: Int, t2: Int -> Pair(t1, t2) }
            .map { Pair(it.first != 0, it.second != 0) }
    }
}