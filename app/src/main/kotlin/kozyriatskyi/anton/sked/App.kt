package kozyriatskyi.anton.sked

import android.content.Context
import android.support.v7.app.AppCompatDelegate
import android.support.v7.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.firebase.analytics.FirebaseAnalytics
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.di.module.StorageModule
import kozyriatskyi.anton.sked.updater.UpdaterJobService
import kozyriatskyi.anton.sked.util.logD


class App : BaseApplication() {

    override fun onCreate() {
        Injector.init(this)
        super.onCreate()

        val preferences = UserSettingsStorage(PreferenceManager.getDefaultSharedPreferences(this))

        applyTheme(preferences)

        PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false)
    }


    private fun applyTheme(sharedPreferences: UserSettingsStorage) {
        val defaultTheme = sharedPreferences.getString(UserSettingsStorage.KEY_DEFAULT_THEME,
                "0").toInt()
        AppCompatDelegate.setDefaultNightMode(defaultTheme)
    }

    override fun onApplicationUpdate(previousVersionName: String, previousVersionCode: Int,
                                     currentVersionName: String, currentVersionCode: Int) {
        relaunchUpdaterJob()

        val preferences = getSharedPreferences(StorageModule.PREFERENCES_USER_INFO, Context.MODE_PRIVATE)
        val userInfoStorage = UserInfoStorage(preferences)

        try {
            val type = userInfoStorage.getUser().type

            this.logD("USER_TYPE: $type")
            val typeStr = if (type == User.Type.STUDENT) "student" else "teacher"
            FirebaseAnalytics.getInstance(this)
                    .setUserProperty("user_type", typeStr)
        } catch (ignore: IllegalStateException) {
            //no user saved - app is launched for the first time
        }
    }

    private fun relaunchUpdaterJob() {
        val firebaseJobDispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        val cancelAllResult = firebaseJobDispatcher.cancelAll()

        when (cancelAllResult) {
            FirebaseJobDispatcher.CANCEL_RESULT_SUCCESS -> {
                Crashlytics.log("Job dispatcher cancel all result: CANCEL_RESULT_SUCCESS")
                logD("Job dispatcher cancel all result: CANCEL_RESULT_SUCCESS")
            }
            FirebaseJobDispatcher.CANCEL_RESULT_UNKNOWN_ERROR -> {
                Crashlytics.log("Job dispatcher cancel all result: CANCEL_RESULT_UNKNOWN_ERROR")
                logD("Job dispatcher cancel all result: CANCEL_RESULT_UNKNOWN_ERROR")
            }
            FirebaseJobDispatcher.CANCEL_RESULT_NO_DRIVER_AVAILABLE -> {
                Crashlytics.log("Job dispatcher cancel all result: CANCEL_RESULT_NO_DRIVER_AVAILABLE")
                logD("Job dispatcher cancel all result: CANCEL_RESULT_NO_DRIVER_AVAILABLE")
            }
        }

        UpdaterJobService.start(this)
    }
}