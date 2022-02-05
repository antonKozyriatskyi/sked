package kozyriatskyi.anton.sked.data.pojo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateScheduleResponse(
    val schedule: List<LessonNetwork>
)
