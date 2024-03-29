package kozyriatskyi.anton.sked.main

import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.common.AppConfigurationManager
import kozyriatskyi.anton.sked.common.SCHEDULE_WEEKS_RANGE
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.FirstDayOfWeekMode
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateManipulator
import java.util.*

class MainInteractor(
    private val scheduleStorage: ScheduleStorage,
    private val lessonMapper: LessonMapper,
    private val scheduleLoader: ScheduleProvider,
    private val userInfoStorage: UserInfoStorage,
    private val dateManipulator: DateManipulator,
    private val appConfigurationManager: AppConfigurationManager,
    private val analyticsManager: AnalyticsManager
) {

    suspend fun updateSchedule(): Result<Unit> {
        return kotlin.runCatching {
            val schedule = scheduleLoader.getSchedule(
                user = userInfoStorage.getUser(),
                startDate = dateManipulator.getFirstDayOfWeekDate(),
                endDate = dateManipulator.getLastDayOfWeekDate(SCHEDULE_WEEKS_RANGE - 1)
            )

            val dbSchedule = lessonMapper.networkToDb(schedule)
            scheduleStorage.saveLessons(dbSchedule)
        }.onFailure {
            analyticsManager.logFailure(
                throwable = it
            )
        }
    }

    fun updateLocale(locale: Locale) {
        dateManipulator.updateLocale(locale)
        appConfigurationManager.updateLocale(locale)
    }

    fun updateFirstDayOfWeekMode(mode: FirstDayOfWeekMode) {
        dateManipulator.updateFirstDayOfWeekMode(mode)
        appConfigurationManager.updateFirstDayOfWeekMode(mode)
    }
}