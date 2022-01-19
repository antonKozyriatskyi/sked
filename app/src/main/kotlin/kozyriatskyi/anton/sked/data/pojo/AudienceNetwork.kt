package kozyriatskyi.anton.sked.data.pojo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AudienceNetwork(
    @Json(name = "number") val number: String,
    @Json(name = "isFree") val isFree: Boolean,
    @Json(name = "note") val note: String,
    @Json(name = "capacity") val capacity: String
)