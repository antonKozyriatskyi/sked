package kozyriatskyi.anton.sked.updater

import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.common.SCHEDULE_WEEKS_RANGE
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateManipulator
import javax.inject.Inject

class UpdaterInteractor @Inject constructor(
    private val scheduleStorage: ScheduleStorage,
    private val lessonMapper: LessonMapper,
    private val scheduleLoader: ScheduleProvider,
    private val userInfoStorage: UserInfoStorage,
    private val dateManipulator: DateManipulator,
    private val analyticsManager: AnalyticsManager
) {

    suspend fun updateSchedule(): Result<Unit> {
        val user = userInfoStorage.getUser()
        val startDate = dateManipulator.getFirstDayOfWeekDate()
        val endDate = dateManipulator.getLastDayOfWeekDate(SCHEDULE_WEEKS_RANGE - 1)

        return kotlin.runCatching {
            val schedule = scheduleLoader.getSchedule(
                user = user,
                startDate = startDate,
                endDate = endDate
            )

            val dbSchedule = lessonMapper.networkToDb(schedule)
            scheduleStorage.saveLessons(dbSchedule)
        }.onFailure {
            analyticsManager.logFailure(
                message = "Couldn't update schedule for user ${user.name} {$user} [$startDate - $endDate]",
                throwable = it
            )
        }
    }
}