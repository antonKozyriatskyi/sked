package kozyriatskyi.anton.sked.byweek

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateUtils

class ByWeekViewInteractor(private val scheduleStorage: ScheduleStorage) {

    fun firstWeekendLessonsCount(): Flow<Pair<Boolean, Boolean>> {
        val thisSat = scheduleStorage.amountOfLessonsOnDate(DateUtils.saturdayDate())
        val thisSun = scheduleStorage.amountOfLessonsOnDate(DateUtils.sundayDate())

        return thisSat.zip(thisSun) { t1: Int, t2: Int -> Pair(t1, t2) }
            .map { Pair(it.first != 0, it.second != 0) }
    }
}