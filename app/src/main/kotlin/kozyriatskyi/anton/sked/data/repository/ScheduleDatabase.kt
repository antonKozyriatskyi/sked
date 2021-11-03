package kozyriatskyi.anton.sked.data.repository

import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.data.LessonsDatabase
import kozyriatskyi.anton.sked.data.pojo.LessonDb
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger
import java.time.LocalDate

/**
 * Created by Anton on 13.07.2017.
 */
class ScheduleDatabase(
    private val database: LessonsDatabase,
    private val timeLogger: ScheduleUpdateTimeLogger
) : ScheduleStorage {

    override fun getLessonsOnDate(date: LocalDate): Flow<List<LessonDb>> =
        database.scheduleDao().observeAllByDate(date)

    override fun getLessonsBetweenDates(start: LocalDate, end: LocalDate): Flow<List<LessonDb>> {
       return database.scheduleDao().observeAllBetweenDates(start, end)
    }

    override fun saveLessons(lessons: List<LessonDb>) {
        with(database.scheduleDao()) {
            deleteAll()
            insertAll(lessons)
        }

        timeLogger.saveTime()
    }

    override fun amountOfLessonsOnDate(date: LocalDate): Flow<Int> =
        database.scheduleDao()
            .amountOfLessonsByDate(date)
}

