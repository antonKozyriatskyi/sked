package kozyriatskyi.anton.sked.data.pojo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimeNetwork(
    @Json(name = "id") val id: String,
    @Json(name = "value") val value: String
)