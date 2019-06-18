package kozyriatskyi.anton.sked.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Anton on 08.03.2017.
 */
object DateUtils {

    private val longDateFormatter by lazy { SimpleDateFormat("dd.MM.yyyy") }
    private val shortDateFormatter by lazy { SimpleDateFormat("dd.MM") }

    fun mondayDate(numberOfWeek: Int = 0, inShortFormat: Boolean = false): String {
        val calendar = getCalendar()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, numberOfWeek)

        return if (inShortFormat)
            shortDateFormatter.format(calendar.time)
        else
            longDateFormatter.format(calendar.time)
    }

    fun tuesdayDate(numberOfWeek: Int = 0, inShortFormat: Boolean = false): String {
        val calendar = getCalendar()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, numberOfWeek)

        return if (inShortFormat)
            shortDateFormatter.format(calendar.time)
        else
            longDateFormatter.format(calendar.time)
    }

    fun wednesdayDate(numberOfWeek: Int = 0, inShortFormat: Boolean = false): String {
        val calendar = getCalendar()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, numberOfWeek)

        return if (inShortFormat)
            shortDateFormatter.format(calendar.time)
        else
            longDateFormatter.format(calendar.time)
    }

    fun thursdayDate(numberOfWeek: Int = 0, inShortFormat: Boolean = false): String {
        val calendar = getCalendar()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, numberOfWeek)

        return if (inShortFormat)
            shortDateFormatter.format(calendar.time)
        else
            longDateFormatter.format(calendar.time)
    }

    fun fridayDate(numberOfWeek: Int = 0, inShortFormat: Boolean = false): String {
        val calendar = getCalendar()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, numberOfWeek)

        return if (inShortFormat)
            shortDateFormatter.format(calendar.time)
        else
            longDateFormatter.format(calendar.time)
    }

    fun saturdayDate(numberOfWeek: Int = 0, inShortFormat: Boolean = false): String {
        val calendar = getCalendar()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, numberOfWeek)

        return if (inShortFormat)
            shortDateFormatter.format(calendar.time)
        else
            longDateFormatter.format(calendar.time)
    }

    fun sundayDate(numberOfWeek: Int = 0, inShortFormat: Boolean = false): String {
        val calendar = getCalendar()
        // if set Calendar.DAY_OF_WEEK as Calendar.SATURDAY directly, it calculates wrong date on pre-marshmallow
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        calendar.add(Calendar.DAY_OF_WEEK, 1)
        calendar.add(Calendar.WEEK_OF_YEAR, numberOfWeek)

        return if (inShortFormat)
            shortDateFormatter.format(calendar.time)
        else
            longDateFormatter.format(calendar.time)
    }

    fun longDateForDayNum(dayNum: Int, weekNum: Int = 0) = when (dayNum) {
        0 -> mondayDate(weekNum)
        1 -> tuesdayDate(weekNum)
        2 -> wednesdayDate(weekNum)
        3 -> thursdayDate(weekNum)
        4 -> fridayDate(weekNum)
        5 -> saturdayDate(weekNum)
        else -> sundayDate(weekNum)
    }

    fun todayDate(): String {
        val day = getCalendar()[Calendar.DAY_OF_WEEK]
        return longDateForDayNum(dayNum = day)
    }

    private fun getCalendar(): Calendar = Calendar.getInstance()
}