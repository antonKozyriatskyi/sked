package kozyriatskyi.anton.sked

import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.di.Injector

class App : BaseApplication() {

    override fun onCreate() {
        Injector.init(this)
        super.onCreate()

        val preferences = UserSettingsStorage(PreferenceManager.getDefaultSharedPreferences(this))

        applyTheme(preferences)

        PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false)

    }

    private fun applyTheme(sharedPreferences: UserSettingsStorage) {
        val defaultTheme = sharedPreferences.getString(UserSettingsStorage.KEY_DEFAULT_THEME, "0").toInt()
        AppCompatDelegate.setDefaultNightMode(defaultTheme)
    }

    override fun onApplicationUpdate(
        previousVersionName: String,
        previousVersionCode: Int,
        currentVersionName: String,
        currentVersionCode: Int
    ) {
        // Sked migrated to WorkManger in v22 so need to launch it
        if (previousVersionCode <= 21 && Injector.appComponent.userInfoStorage().hasUser()) {
            Injector.appComponent.jobManager().launchUpdaterJob()
        }
    }
}