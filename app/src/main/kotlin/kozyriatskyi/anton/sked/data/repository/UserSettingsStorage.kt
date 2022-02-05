package kozyriatskyi.anton.sked.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@Suppress("SameParameterValue")
class UserSettingsStorage(private val preferences: SharedPreferences) {

    companion object {
        const val KEY_DEFAULT_VIEW_MODE = "default_view_mode"
        const val KEY_DEFAULT_THEME = "default_theme"
        const val KEY_NOTIFY_ON_UPDATE = "notify_on_update"

        const val VIEW_BY_DAY = 0
        const val VIEW_BY_WEEK = 1

        const val THEME_AUTO = AppCompatDelegate.MODE_NIGHT_AUTO
        const val THEME_LIGHT = AppCompatDelegate.MODE_NIGHT_NO
        const val THEME_DARK = AppCompatDelegate.MODE_NIGHT_YES

        private const val START_DAY_KEY = "start_day"
    }

    val shouldSendUpdateNotification: Boolean get() = getBoolean(KEY_NOTIFY_ON_UPDATE, true)

    private val firstDayOfWeekMode: FirstDayOfWeekMode
        get() {
            return FirstDayOfWeekMode.values()[getString(START_DAY_KEY, "0").toInt()]
        }

    fun getString(key: String, default: String): String = preferences.getString(key, default)!!

    fun getInt(key: String, default: Int): Int = preferences.getInt(key, default)

    private fun getBoolean(key: String, default: Boolean): Boolean = preferences.getBoolean(key, default)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeFirstDayOfWeek(): Flow<FirstDayOfWeekMode> = callbackFlow {
        // Dispatch initial value
        channel.trySend(firstDayOfWeekMode)

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == START_DAY_KEY) {
                channel.trySend(firstDayOfWeekMode)
            }
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }
}