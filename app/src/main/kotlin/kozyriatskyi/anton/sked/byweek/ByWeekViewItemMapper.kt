package kozyriatskyi.anton.sked.byweek

import kozyriatskyi.anton.sked.util.DateFormatter
import java.time.LocalDate

class ByWeekViewItemMapper(private val dateFormatter: DateFormatter) {

    fun create(dates: List<LocalDate>): ByWeekViewItem = ByWeekViewItem(
        title = getTitle(dates.first(), dates.last()),
        dates = dates
    )

    private fun getTitle(startDate: LocalDate, endDate: LocalDate): String {
        return "${dateFormatter.short(startDate)} - ${dateFormatter.short(endDate)}"
    }
}