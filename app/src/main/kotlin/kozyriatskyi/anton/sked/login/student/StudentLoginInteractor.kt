package kozyriatskyi.anton.sked.login.student

import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.common.SCHEDULE_WEEKS_RANGE
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.Student
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.repository.StudentInfoProvider
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.JobManager

class StudentLoginInteractor(
    private val studentInfoProvider: StudentInfoProvider, // to get info from
    private val userUserInfoStorage: UserInfoStorage, // to write info into
    private val scheduleProvider: ScheduleProvider, // to get lessons from
    private val scheduleRepository: ScheduleStorage, // to write lessons into
    private val connectionStateProvider: ConnectionStateProvider,
    private val mapper: LessonMapper,
    private val jobManager: JobManager,
    private val analyticsManager: AnalyticsManager,
    private val dateManipulator: DateManipulator
) {

    fun connectionStateChanges(): Flow<Boolean> = connectionStateProvider.connectionStateChanges()

    suspend fun loadFaculties(): Result<List<Item>> = kotlin.runCatching {
        studentInfoProvider.getFaculties()
    }
        .onFailure {
            analyticsManager.logFailure(
                message = "Students faculties list loading failed:",
                throwable = it
            )
        }

    suspend fun loadCourses(facultyId: String): Result<List<Item>> = kotlin.runCatching {
        studentInfoProvider.getCourses(facultyId)
    }
        .onFailure {
            val msg = """
                Students courses list loading failed:
                facultyId: $facultyId
                """.trimIndent()

            analyticsManager.logFailure(
                message = msg,
                throwable = it
            )
        }

    suspend fun loadGroups(facultyId: String, courseId: String): Result<List<Item>> = kotlin.runCatching {
        studentInfoProvider.getGroups(facultyId, courseId)
    }
        .onFailure {
            val msg = """
                Students groups list loading failed:
                courseId: $courseId
                """.trimIndent()

            analyticsManager.logFailure(
                message = msg,
                throwable = it
            )
        }

    suspend fun loadSchedule(student: Student): Result<List<LessonNetwork>> = kotlin.runCatching {
        scheduleProvider.getSchedule(
            user = student,
            startDate = dateManipulator.getFirstDayOfWeekDate(),
            endDate = dateManipulator.getLastDayOfWeekDate(SCHEDULE_WEEKS_RANGE - 1)
        )
    }
        .onSuccess {
            scheduleRepository.saveLessons(mapper.networkToDb(it))

            jobManager.launchUpdaterJob()
            analyticsManager.logUserType(AnalyticsManager.UserType.Student)
            userUserInfoStorage.saveUser(student)

        }.onFailure {
            val msg = """
            Student schedule loading failed:
            faculty: ${student.faculty}
            facultyId: ${student.facultyId}
            course: ${student.course}
            courseId: ${student.courseId}
            group: ${student.group}
            groupId: ${student.groupId}
            """.trimIndent()

            analyticsManager.logFailure(
                message = msg,
                throwable = it
            )
        }
}

