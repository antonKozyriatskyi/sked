package kozyriatskyi.anton.sked.data.pojo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetClassroomsTimesResponse(
    @Json(name = "from") val from: List<TimeNetwork>,
    @Json(name = "to") val to: List<TimeNetwork>
)