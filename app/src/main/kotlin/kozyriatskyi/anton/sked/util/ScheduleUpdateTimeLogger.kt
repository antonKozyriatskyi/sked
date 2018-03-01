package kozyriatskyi.anton.sked.util

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class ScheduleUpdateTimeLogger(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val UPDATE_TIME = "update_time"
    }

    fun saveTime() {
        val formatter = SimpleDateFormat("HH:mm | dd.MM.yyyy", Locale.getDefault())
        val formattedTime = formatter.format(Date())

        sharedPreferences.edit {
            putString(UPDATE_TIME, formattedTime)
        }
    }

    fun getTime(): String? {
        val time = sharedPreferences.getString(UPDATE_TIME, "")
        return if (time == "") null else time
    }
}