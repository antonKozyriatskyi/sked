package kozyriatskyi.anton.sked.byday

import kozyriatskyi.anton.sked.R
import java.time.DayOfWeek
import java.time.LocalDate

class ByDayViewItemMapper {

    fun create(date: LocalDate): ByDayViewItem {
        return ByDayViewItem(
            title = getName(date.dayOfWeek),
            date = date
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