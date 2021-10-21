package kozyriatskyi.anton.sked

import android.app.Application
import android.content.Context
import kozyriatskyi.anton.sked.util.edit

private const val SHARE_PREFS_NAME = "versions"
private const val VERSION_CODE_KEY = "version_code"
private const val VERSION_NAME_KEY = "version_name"

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        checkAppVersion()
    }

    @Suppress("SameParameterValue")
    protected open fun onApplicationUpdate(
        previousVersionName: String, previousVersionCode: Int,
        currentVersionName: String, currentVersionCode: Int
    ) { }

    private fun checkAppVersion() {
        val preferences = getSharedPreferences(SHARE_PREFS_NAME, Context.MODE_PRIVATE)

        val currentVersionCode = BuildConfig.VERSION_CODE
        val previousVersionCode = preferences.getInt(VERSION_CODE_KEY, 0)

        if (previousVersionCode != currentVersionCode) {
            val currentVersionName = BuildConfig.VERSION_NAME
            val previousVersionName = preferences.getString(VERSION_NAME_KEY, "")!!

            preferences.edit {
                putString(VERSION_NAME_KEY, currentVersionName)
                putInt(VERSION_CODE_KEY, currentVersionCode)
            }

            onApplicationUpdate(
                previousVersionName, previousVersionCode,
                currentVersionName, currentVersionCode
            )
        }
    }
}