package kozyriatskyi.anton.sked.login.teacher

import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.Teacher
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.repository.TeacherInfoProvider
import kozyriatskyi.anton.sked.util.FirebaseAnalyticsLogger
import kozyriatskyi.anton.sked.util.JobManager

class TeacherLoginInteractor(
    private val teacherInfoProvider: TeacherInfoProvider,
    private val userInfoStorage: UserInfoStorage, // to write info into
    private val scheduleProvider: ScheduleProvider, // to get lessons from
    private val scheduleRepository: ScheduleStorage, // to write lessons into
    private val connectionStateProvider: ConnectionStateProvider,
    private val mapper: LessonMapper,
    private val jobManager: JobManager,
    private val logger: FirebaseAnalyticsLogger
) {

    fun connectionStateChanges(): Flow<Boolean> = connectionStateProvider.connectionStateChanges()

    fun loadDepartments(): Result<List<Item>> = kotlin.runCatching {
        teacherInfoProvider.getDepartments()
    }

    fun loadTeachers(departmentId: String): Result<List<Item>> = kotlin.runCatching {
        teacherInfoProvider.getTeachers(departmentId)
    }

    fun loadSchedule(teacher: Teacher): Result<List<LessonNetwork>> {
        return kotlin.runCatching {
            scheduleProvider.getSchedule(teacher)
        }.onSuccess {
            scheduleRepository.saveLessons(mapper.networkToDb(it))

            jobManager.launchUpdaterJob()
            logger.logTeacher()
            userInfoStorage.saveUser(teacher)
        }.onFailure {
            FirebaseCrashlytics.getInstance().recordException(it)
        }
    }
}