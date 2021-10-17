package kozyriatskyi.anton.sked.login.student

import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.Student
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.repository.StudentInfoProvider
import kozyriatskyi.anton.sked.util.FirebaseAnalyticsLogger
import kozyriatskyi.anton.sked.util.JobManager

class StudentLoginInteractor(
    private val studentInfoProvider: StudentInfoProvider, // to get info from
    private val userUserInfoStorage: UserInfoStorage, // to write info into
    private val scheduleProvider: ScheduleProvider, // to get lessons from
    private val scheduleRepository: ScheduleStorage, // to write lessons into
    private val connectionStateProvider: ConnectionStateProvider,
    private val mapper: LessonMapper,
    private val jobManager: JobManager,
    private val logger: FirebaseAnalyticsLogger
) {

    fun connectionStateChanges(): Flow<Boolean> = connectionStateProvider.connectionStateChanges()

    fun loadFaculties(): Result<List<Item>> = kotlin.runCatching {
        studentInfoProvider.getFaculties()
    }

    fun loadCourses(facultyId: String): Result<List<Item>> = kotlin.runCatching {
        studentInfoProvider.getCourses(facultyId)
    }

    fun loadGroups(courseId: String): Result<List<Item>> = kotlin.runCatching {
        studentInfoProvider.getGroups(courseId)
    }

    fun loadSchedule(student: Student): Result<List<LessonNetwork>> = kotlin.runCatching {
        scheduleProvider.getSchedule(student)
    }.onSuccess {
        scheduleRepository.saveLessons(mapper.networkToDb(it))

        jobManager.launchUpdaterJob()
        logger.logStudent()
        userUserInfoStorage.saveUser(student)
    }.onFailure {
        FirebaseCrashlytics.getInstance().recordException(it)
    }
}

