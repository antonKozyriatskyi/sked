package kozyriatskyi.anton.sked.byday

import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.LessonDb
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import java.time.DayOfWeek
import java.time.LocalDate

class ByDayViewItemMapper(
    private val dayMapper: DayMapper
) {

    fun create(date: LocalDate, lessons: List<LessonDb>): ByDayViewItem {
        return ByDayViewItem(
            title = getName(date.dayOfWeek),
            day = dayMapper.createUiModel(date = date, lessons = lessons)
        )
    }

    private fun getName(dayOfWeek: DayOfWeek): Int {
        val daysOfWeek = arrayOf(
            R.string.day_of_week_monday,
            R.string.day_of_week_tuesday,
            R.string.day_of_week_wednesday,
            R.string.day_of_week_thursday,
            R.string.day_of_week_friday,
            R.string.day_of_week_saturday,
            R.string.day_of_week_sunday
        )

        return daysOfWeek[DayOfWeek.values().indexOf(dayOfWeek)]
    }
}