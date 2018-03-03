package kozyriatskyi.anton.sked

import android.content.Context
import android.support.v7.app.AppCompatDelegate
import android.support.v7.preference.PreferenceManager
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.di.module.StorageModule
import kozyriatskyi.anton.sked.repository.ScheduleLoader
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger
import kozyriatskyi.anton.sked.updater.UpdaterJobService
import kozyriatskyi.anton.sked.util.logD
import kozyriatskyi.anton.sked.util.logE


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

        //update the schedule
        val updaterJobComponent = Injector.updaterJobComponent()

        val scheduleLoader: ScheduleLoader = updaterJobComponent.scheduleLoader()
        val userInfoPreferences: UserInfoStorage = updaterJobComponent.userInfoPreferences()
        val timeLogger: ScheduleUpdateTimeLogger = updaterJobComponent.timeLogger()

        Thread {
            try {
                val user = userInfoPreferences.getUser()
                scheduleLoader.getSchedule(user)
                timeLogger.saveTime()
            } catch (t: Throwable) {
                logE("Error updating schedule: ${t.message}", t, tag = "TAG")
                FirebaseCrash.report(t)
            }
        }.start()
    }

    private fun relaunchUpdaterJob() {
        val firebaseJobDispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        val cancelAllResult = firebaseJobDispatcher.cancelAll()

        when (cancelAllResult) {
            FirebaseJobDispatcher.CANCEL_RESULT_SUCCESS -> {
                FirebaseCrash.log("Job dispatcher cancel all result: CANCEL_RESULT_SUCCESS")
                logD("Job dispatcher cancel all result: CANCEL_RESULT_SUCCESS")
            }
            FirebaseJobDispatcher.CANCEL_RESULT_UNKNOWN_ERROR -> {
                FirebaseCrash.log("Job dispatcher cancel all result: CANCEL_RESULT_UNKNOWN_ERROR")
                logD("Job dispatcher cancel all result: CANCEL_RESULT_UNKNOWN_ERROR")
            }
            FirebaseJobDispatcher.CANCEL_RESULT_NO_DRIVER_AVAILABLE -> {
                FirebaseCrash.log("Job dispatcher cancel all result: CANCEL_RESULT_NO_DRIVER_AVAILABLE")
                logD("Job dispatcher cancel all result: CANCEL_RESULT_NO_DRIVER_AVAILABLE")
            }
        }

        UpdaterJobService.start(this)
    }
}