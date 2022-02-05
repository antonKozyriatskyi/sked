package kozyriatskyi.anton.sked.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateFormatter {

    private val longPattern by lazy { DateTimeFormatter.ofPattern("dd.MM.yyyy") }

    private val shortPattern by lazy { DateTimeFormatter.ofPattern("dd.MM") }

    fun short(date: LocalDate): String = date.format(shortPattern)

    fun long(date: LocalDate): String = date.format(longPattern)

    fun parse(longDate: String): LocalDate = LocalDate.parse(longDate, longPattern)

}