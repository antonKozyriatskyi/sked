package kozyriatskyi.anton.sked.updater

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.common.SCHEDULE_WEEKS_RANGE
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.main.MainActivity
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger
import javax.inject.Inject


/**
 * Created by Anton on 06.09.2017.
 */

class UpdaterJobService(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object {
        private const val CHANNEL_ID = "updater_02"
        private const val NOTIFICATION_ID = 1
    }

    @Inject
    lateinit var scheduleLoader: ScheduleProvider

    @Inject
    lateinit var userPreferences: UserSettingsStorage

    @Inject
    lateinit var userInfoPreferences: UserInfoStorage

    @Inject
    lateinit var timeLogger: ScheduleUpdateTimeLogger

    @Inject
    lateinit var dateManipulator: DateManipulator

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    override suspend fun doWork(): Result {
        Injector.inject(this)

        val updateSuccessful = withContext(Dispatchers.IO) {
            val user = userInfoPreferences.getUser()
            val startDate = dateManipulator.getFirstDayOfWeekDate()
            val endDate = dateManipulator.getLastDayOfWeekDate(SCHEDULE_WEEKS_RANGE - 1)

            kotlin.runCatching {
                scheduleLoader.getSchedule(
                    user,
                    startDate = startDate,
                    endDate = endDate
                )
            }
                .onSuccess {
                    timeLogger.saveTime()
                }
                .onFailure {
                    analyticsManager.logFailure(
                        message = "Couldn't update schedule for user ${user.name} {$user} [$startDate - $endDate]",
                        throwable = it
                    )
                }
                .isSuccess
        }

        val notifyOnUpdate = userPreferences.shouldSendUpdateNotification
        if (notifyOnUpdate) {
            sendNotification(updateSuccessful, applicationContext)
        }

        return if (updateSuccessful) Result.success() else Result.retry()
    }

    private fun sendNotification(successfullyUpdated: Boolean, context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            flags
        )

        val channelName = context.getString(R.string.notification_channel_name)
        val channelDescriptions = context.getString(R.string.notification_channel_description)
        val contentText = when {
                successfullyUpdated -> R.string.notification_schedule_updated_successfully
                else -> R.string.notification_schedule_updated_unsuccessfully
            }.let(context::getString)

        val manager = NotificationManagerCompat.from(context)

        val channel = NotificationChannelCompat
            .Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_MIN)
            .setName(channelName)
            .setDescription(channelDescriptions)
            .setLightsEnabled(true)
            .setLightColor(Color.GREEN)
            .build()

        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_schedule_updated_title))
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_notif_update)
            .setColor(ContextCompat.getColor(context, R.color.primary))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()

        manager.notify(NOTIFICATION_ID, notification)
    }
}