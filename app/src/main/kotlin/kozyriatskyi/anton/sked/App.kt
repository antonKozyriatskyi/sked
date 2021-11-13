package kozyriatskyi.anton.sked

import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.util.JobManager


class App : BaseApplication() {

    override fun onCreate() {
        Injector.init(this)
        super.onCreate()

        val preferences = UserSettingsStorage(PreferenceManager.getDefaultSharedPreferences(this))

        applyTheme(preferences)

        PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false)

        relaunchUpdaterJob()
    }


    private fun applyTheme(sharedPreferences: UserSettingsStorage) {
        val defaultTheme = sharedPreferences.getString(UserSettingsStorage.KEY_DEFAULT_THEME, "0").toInt()
        AppCompatDelegate.setDefaultNightMode(defaultTheme)
    }

    // TODO Remove this in a next update
    private fun relaunchUpdaterJob() {
        JobManager(this).launchUpdaterJob()
    }
}