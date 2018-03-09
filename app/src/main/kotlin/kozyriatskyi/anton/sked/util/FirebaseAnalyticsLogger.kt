package kozyriatskyi.anton.sked.util

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsLogger(private val context: Context) {

    fun logStudent() {
        FirebaseAnalytics.getInstance(context).setUserProperty("user_type", "student")
    }

    fun logTeacher() {
        FirebaseAnalytics.getInstance(context).setUserProperty("user_type", "teacher")
    }
}