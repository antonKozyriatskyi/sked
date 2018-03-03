package kozyriatskyi.anton.sked.util

import android.content.Context
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import kozyriatskyi.anton.sked.updater.UpdaterJobService

class JobManager(private val context: Context) {

    fun launchUpdaterJob() {
        val cancelAllResult = FirebaseJobDispatcher(GooglePlayDriver(context)).cancelAll()

        when (cancelAllResult) {
            FirebaseJobDispatcher.CANCEL_RESULT_SUCCESS ->
                logI("Job dispatcher cancel all result: CANCEL_RESULT_SUCCESS")
            FirebaseJobDispatcher.CANCEL_RESULT_UNKNOWN_ERROR ->
                logI("Job dispatcher cancel all result: CANCEL_RESULT_UNKNOWN_ERROR")
            FirebaseJobDispatcher.CANCEL_RESULT_NO_DRIVER_AVAILABLE ->
                logI("Job dispatcher cancel all result: CANCEL_RESULT_NO_DRIVER_AVAILABLE")
        }

        UpdaterJobService.start(context)
    }
}