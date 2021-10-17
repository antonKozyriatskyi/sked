package kozyriatskyi.anton.sked.repository

import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.data.pojo.LessonDb

interface ScheduleStorage {

    // date in dd.MM.yyyy format
    fun getLessonsByDate(date: String): Flow<List<LessonDb>>

    fun saveLessons(lessons: List<LessonDb>)

    fun amountOfLessonsOnDate(date: String): Flow<Int>
}