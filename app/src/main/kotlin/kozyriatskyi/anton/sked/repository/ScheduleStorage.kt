package kozyriatskyi.anton.sked.repository

import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.data.pojo.LessonDb
import java.time.LocalDate

interface ScheduleStorage {

    fun getLessonsOnDate(date: LocalDate): Flow<List<LessonDb>>

    fun getLessonsBetweenDates(start: LocalDate, end: LocalDate): Flow<List<LessonDb>>

    fun saveLessons(lessons: List<LessonDb>)

    fun amountOfLessonsOnDate(date: LocalDate): Flow<Int>
}