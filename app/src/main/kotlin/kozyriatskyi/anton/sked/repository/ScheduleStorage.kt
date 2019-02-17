package kozyriatskyi.anton.sked.repository

import io.reactivex.Observable
import kozyriatskyi.anton.sked.data.pojo.LessonDb

interface ScheduleStorage {

    // date in dd.MM.yyyy format
    fun getLessonsByDate(date: String): Observable<List<LessonDb>>

    fun saveLessons(lessons: List<LessonDb>)

    fun amountOfLessonsOnDate(date: String): Observable<Int>
}