package kozyriatskyi.anton.sked.data.pojo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Anton on 23.02.2017.
 */

/**
 * Represents a single faculty, course, group or student
 */
data class Item(val id: String, val value: String)

@JsonClass(generateAdapter = true)
data class LoginItem(
    @Json(name = "id") val id: String,
    @Json(name = "value") val value: String
)