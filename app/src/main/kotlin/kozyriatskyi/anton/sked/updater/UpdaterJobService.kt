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
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.main.MainActivity
import javax.inject.Inject


/**
 * Created by Anton on 06.09.2017.
 */

class UpdaterJobService @Inject constructor(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        private const val CHANNEL_ID = "updater_02"
        private const val NOTIFICATION_ID = 1
    }

    @Inject
    lateinit var interactor: UpdaterInteractor

    @Inject
    lateinit var userPreferences: UserSettingsStorage

    override suspend fun doWork(): Result {
        Injector.inject(this)

        val updateSuccessful = withContext(Dispatchers.IO) {
            kotlin.runCatching {
                interactor.updateSchedule()
            }.isSuccess
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
            .setColor(
                MaterialColors.getColor(
                    applicationContext,
                    R.attr.colorPrimary,
                    ContextCompat.getColor(context, R.color.teal500)
                )
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()

        manager.notify(NOTIFICATION_ID, notification)
    }
}