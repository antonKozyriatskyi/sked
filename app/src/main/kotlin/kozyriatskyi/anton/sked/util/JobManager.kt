package kozyriatskyi.anton.sked.util

import android.content.Context
import androidx.work.*
import kozyriatskyi.anton.sked.updater.UpdaterJobService
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

private const val UNIQUE_WORK_NAME = "update-schedule"

class JobManager(private val context: Context) {

    fun launchUpdaterJob() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val request = PeriodicWorkRequestBuilder<UpdaterJobService>(Duration.ofDays(1))
            .setConstraints(constraints)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.SECONDS)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10L, TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
    }


    private fun calculateInitialDelay(): Long {
        val now = LocalDateTime.now()

        var targetTime = LocalDateTime.now()
            .withHour(18)
            .withMinute(0)
            .withSecond(0)

        if (targetTime.isBefore(now)) {
            targetTime = targetTime.plusDays(1)
        }

        return now.until(targetTime, ChronoUnit.SECONDS)
    }
}