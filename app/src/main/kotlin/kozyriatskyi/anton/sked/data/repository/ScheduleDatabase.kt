package kozyriatskyi.anton.sked.data.repository

import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.data.LessonsDatabase
import kozyriatskyi.anton.sked.data.pojo.LessonDb
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger

/**
 * Created by Anton on 13.07.2017.
 */
class ScheduleDatabase(private val database: LessonsDatabase,
                       private val timeLogger: ScheduleUpdateTimeLogger) : ScheduleStorage {

    override fun getLessonsByDate(date: String): Flow<List<LessonDb>> =
            database.scheduleDao().observeAllByDate(date)

    override fun saveLessons(lessons: List<LessonDb>) {
        with(database.scheduleDao()) {
            deleteAll()
            insertAll(lessons)
        }

        timeLogger.saveTime()
    }

    override fun amountOfLessonsOnDate(date: String): Flow<Int> =
            database.scheduleDao()
            .amountOfLessonsByDate(date)
}

