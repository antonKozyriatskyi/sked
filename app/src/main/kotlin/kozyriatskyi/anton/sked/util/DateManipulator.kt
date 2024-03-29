package kozyriatskyi.anton.sked.util

import kozyriatskyi.anton.sked.data.repository.FirstDayOfWeekMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*

class DateManipulator {

    private var locale: Locale = Locale.getDefault()

    private var firstDayOfWeekMode: FirstDayOfWeekMode = FirstDayOfWeekMode.Monday

    private var weekFields: WeekFields = WeekFields.ISO

    fun today(): LocalDate = LocalDate.now()

    fun isWorkday(date: LocalDate): Boolean = isWeekend(date).not()

    fun isWeekend(date: LocalDate): Boolean = isSaturday(date) || isSunday(date)

    private fun isSaturday(date: LocalDate): Boolean = date.dayOfWeek == DayOfWeek.SATURDAY

    private fun isSunday(date: LocalDate): Boolean = date.dayOfWeek == DayOfWeek.SUNDAY

    @OptIn(ExperimentalStdlibApi::class)
    fun getRemainingWeekends(date: LocalDate): List<LocalDate> {
        val saturdayDate =
            date.with(weekFields.dayOfWeek(), DayOfWeek.SATURDAY.getLong(weekFields.dayOfWeek()))
        val sundayDate =
            date.with(weekFields.dayOfWeek(), DayOfWeek.SUNDAY.getLong(weekFields.dayOfWeek()))

        return buildList {
            if (saturdayDate.isAfter(date) || saturdayDate.isEqual(date)) {
                add(saturdayDate)
            }

            if (sundayDate.isAfter(date) || sundayDate.isEqual(date)) {
                add(sundayDate)
            }
        }
    }

    fun getWeekRange(week: Int = 0): List<LocalDate> = List(7) {
        dateOfDay(weekFields.firstDayOfWeek.plus(it.toLong()), week)
    }

    fun getFirstDayOfWeekDate(week: Int = 0): LocalDate {
        return dateOfDay(weekFields.firstDayOfWeek, week)
    }

    fun getLastDayOfWeekDate(week: Int = 0): LocalDate {
        return dateOfDay(weekFields.firstDayOfWeek.plus(6), week)
    }

    private fun dateOfDay(dayOfWeek: DayOfWeek, addWeeks: Int): LocalDate {
        val today = today()

        val adjuster =
            if (today.dayOfWeek.get(weekFields.dayOfWeek()) >= dayOfWeek.get(weekFields.dayOfWeek())) {
                TemporalAdjusters.previousOrSame(dayOfWeek)
            } else {
                TemporalAdjusters.next(dayOfWeek)
            }

        return today
            .with(adjuster)
            .plusWeeks(addWeeks.toLong())
    }

    fun updateFirstDayOfWeekMode(dayOfWeekModeMode: FirstDayOfWeekMode) {
        weekFields = when (dayOfWeekModeMode) {
            FirstDayOfWeekMode.Monday -> WeekFields.ISO
            FirstDayOfWeekMode.Sunday -> WeekFields.SUNDAY_START
            FirstDayOfWeekMode.LocaleBased -> WeekFields.of(locale)
        }
    }

    fun updateLocale(locale: Locale) {
        if (locale == this.locale) return

        this.locale = locale

        if (firstDayOfWeekMode == FirstDayOfWeekMode.LocaleBased) {
            weekFields = WeekFields.of(locale)
        }
    }
}
