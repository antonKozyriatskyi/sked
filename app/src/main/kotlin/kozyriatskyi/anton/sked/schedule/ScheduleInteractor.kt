package kozyriatskyi.anton.sked.schedule

import kozyriatskyi.anton.sked.common.AppConfigurationManager
import kozyriatskyi.anton.sked.common.SCHEDULE_WEEKS_RANGE
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.FirstDayOfWeekMode
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateManipulator

class ScheduleInteractor(
    private val scheduleStorage: ScheduleStorage,
    private val lessonMapper: LessonMapper,
    private val scheduleLoader: ScheduleProvider,
    private val userInfoStorage: UserInfoStorage,
    private val dateManipulator: DateManipulator,
    private val appConfigurationManager: AppConfigurationManager
) {

    fun updateSchedule(): Result<Unit> = kotlin.runCatching {
        val schedule = scheduleLoader.getSchedule(
            user = userInfoStorage.getUser(),
            startDate = dateManipulator.getFirstDayOfWeekDate(),
            endDate = dateManipulator.getLastDayOfWeekDate(SCHEDULE_WEEKS_RANGE - 1)
        )

        val dbSchedule = lessonMapper.networkToDb(schedule)
        scheduleStorage.saveLessons(dbSchedule)
    }

    fun updateFirstDayOfWeekMode(mode: FirstDayOfWeekMode) {
        dateManipulator.updateFirstDayOfWeekMode(mode)
        appConfigurationManager.updateFirstDayOfWeekMode(mode)
    }
}