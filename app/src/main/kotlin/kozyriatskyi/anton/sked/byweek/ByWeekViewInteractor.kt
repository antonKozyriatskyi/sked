package kozyriatskyi.anton.sked.byweek

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import java.time.LocalDate

class ByWeekViewInteractor(private val scheduleStorage: ScheduleStorage) {

    fun hasLessonsOnDate(date: LocalDate): Flow<Boolean> {
        return scheduleStorage.amountOfLessonsOnDate(date).map { it > 0 }
    }
}