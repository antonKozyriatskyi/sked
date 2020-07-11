package kozyriatskyi.anton.sked.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

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
    }

    fun getString(key: String, default: String): String = preferences.getString(key, default)!!

    fun getInt(key: String, default: Int): Int = preferences.getInt(key, default)

    fun getBoolean(key: String, default: Boolean): Boolean = preferences.getBoolean(key, default)

}