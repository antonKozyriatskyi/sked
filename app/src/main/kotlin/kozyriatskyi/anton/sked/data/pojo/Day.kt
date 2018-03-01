package kozyriatskyi.anton.sked.data.pojo

import kozyriatskyi.anton.sked.data.repository.DateFormatter

/**
 * Created by Anton on 22.08.2017.
 */
open class Day(val dayNumber: Int, val weekNumber: Int, val date: String, val lessons: List<LessonDb>)

open class DayUi(val dayNum: Int, val weekNumber: Int, val day: String, val shortDate: String,
                 val lessons: List<LessonUi>)

open class DayMapper(private val lessonMapper: LessonMapper, private val dateFormatter: DateFormatter) {

    open fun dbToUi(day: Day): DayUi =
            DayUi(day.dayNumber, day.weekNumber, dateFormatter.dayOfWeek(day.dayNumber),
                    dateFormatter.shortDate(day.date), lessonMapper.dbToView(day.lessons))

    open fun dbToUi(days: List<Day>): List<DayUi> = days.map {
        DayUi(it.dayNumber, it.weekNumber, dateFormatter.dayOfWeek(it.dayNumber),
                dateFormatter.shortDate(it.date), lessonMapper.dbToView(it.lessons))
    }
}