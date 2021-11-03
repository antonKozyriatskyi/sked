package kozyriatskyi.anton.sked.byday

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import java.time.LocalDate

class ByDayViewInteractor(private val scheduleStorage: ScheduleStorage) {

    fun hasLessonsOnDate(date: LocalDate): Flow<Boolean> {
        return scheduleStorage.amountOfLessonsOnDate(date).map { it > 0 }
    }
}