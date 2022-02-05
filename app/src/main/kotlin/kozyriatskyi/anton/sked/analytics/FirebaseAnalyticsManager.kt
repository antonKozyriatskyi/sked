package kozyriatskyi.anton.sked.analytics

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

private const val USER_TYPE_TAG = "user_type"
private const val INFO_LOG_TAG = "log_info"
private const val INFO_LOG_MSG_KEY = "log_info"

class FirebaseAnalyticsManager(
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics
) : AnalyticsManager {

    override fun logInfo(message: String) {
        analytics.logEvent(
            INFO_LOG_TAG,
            bundleOf(INFO_LOG_MSG_KEY to message)
        )
    }

    override fun logUserType(type: AnalyticsManager.UserType) {
        val userType = type.stringValue()
        analytics.setUserProperty(USER_TYPE_TAG, userType)

        analytics.logEvent(
            FirebaseAnalytics.Event.LOGIN,
            bundleOf(USER_TYPE_TAG to userType)
        )
    }

    override fun logFailure(message: String?, throwable: Throwable?) {
        message?.let(crashlytics::log)
        throwable?.let(crashlytics::recordException)
    }

    private fun AnalyticsManager.UserType.stringValue(): String {
        return when (this) {
            AnalyticsManager.UserType.Student -> "student"
            AnalyticsManager.UserType.Teacher -> "teacher"
        }
    }
}