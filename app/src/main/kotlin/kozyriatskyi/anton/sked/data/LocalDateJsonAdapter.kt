package kozyriatskyi.anton.sked.data

import com.squareup.moshi.*
import kozyriatskyi.anton.sked.util.DateFormatter
import java.time.LocalDate

class LocalDateJsonAdapter(
    private val dateFormatter: DateFormatter
) : JsonAdapter<LocalDate>() {
    @FromJson
    override fun fromJson(reader: JsonReader): LocalDate? {
        return try {
            val dateAsString = reader.nextString()
            dateFormatter.parse(dateAsString)
        } catch (e: Exception) {
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        if (value != null) {
            writer.value(dateFormatter.long(value))
        }
    }
}