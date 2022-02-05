package kozyriatskyi.anton.sked.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateSqliteConverter {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun deserialize(value: String): LocalDate = LocalDate.parse(value, formatter)

    @TypeConverter
    fun serialize(date: LocalDate): String = date.format(formatter)
}