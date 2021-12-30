package kozyriatskyi.anton.sked.screen.login.teacher

import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.common.SCHEDULE_WEEKS_RANGE
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.Teacher
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.repository.TeacherInfoProvider
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.JobManager

class TeacherLoginInteractor(
    private val teacherInfoProvider: TeacherInfoProvider,
    private val userInfoStorage: UserInfoStorage,
    private val scheduleProvider: ScheduleProvider,
    private val scheduleRepository: ScheduleStorage,
    private val connectionStateProvider: ConnectionStateProvider,
    private val mapper: LessonMapper,
    private val jobManager: JobManager,
    private val analyticsManager: AnalyticsManager,
    private val dateManipulator: DateManipulator
) {

    fun connectionStateChanges(): Flow<Boolean> = connectionStateProvider.connectionStateChanges()

    fun loadDepartments(): Result<List<Item>> = kotlin.runCatching {
        teacherInfoProvider.getDepartments()
    }.onFailure {
        analyticsManager.logFailure(
            message = "Teachers departments loading failed",
            throwable = it
        )
    }

    fun loadTeachers(departmentId: String): Result<List<Item>> = kotlin.runCatching {
        teacherInfoProvider.getTeachers(departmentId)
    }.onFailure {
        val msg = """
                Teachers list loading failed:
                departmentId: $departmentId
                """.trimIndent()

        analyticsManager.logFailure(
            message = msg,
            throwable = it
        )
    }

    fun loadSchedule(teacher: Teacher): Result<List<LessonNetwork>> {
        return kotlin.runCatching {
            scheduleProvider.getSchedule(
                user = teacher,
                startDate = dateManipulator.getFirstDayOfWeekDate(),
                endDate = dateManipulator.getLastDayOfWeekDate(SCHEDULE_WEEKS_RANGE - 1)
            )
        }.onSuccess {
            scheduleRepository.saveLessons(mapper.networkToDb(it))

            jobManager.launchUpdaterJob()
            analyticsManager.logUserType(AnalyticsManager.UserType.Teacher)
            userInfoStorage.saveUser(teacher)

        }.onFailure {
            val msg = """
                Teacher schedule loading failed:
                department: ${teacher.department}
                departmentId: ${teacher.departmentId}
                teacher: ${teacher.teacher}
                teacherId: ${teacher.teacherId}
                """.trimIndent()

            analyticsManager.logFailure(
                message = msg,
                throwable = it
            )
        }
    }
}