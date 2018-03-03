package kozyriatskyi.anton.sked.login.teacher

import com.google.firebase.crash.FirebaseCrash
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.pojo.Teacher
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleLoader
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.repository.TeacherInfoLoader
import kozyriatskyi.anton.sked.util.FirebaseLogger
import kozyriatskyi.anton.sked.util.JobManager
import java.util.*

class TeacherLoginInteractor(private val teacherInfoProvider: TeacherInfoLoader,
                             private val userInfoStorage: UserInfoStorage, // to write info into
                             private val teacherNetworkScheduleProvider: ScheduleLoader, // to get lessons from
                             private val scheduleRepository: ScheduleStorage, // to write lessons into
                             private val connectionStateProvider: ConnectionStateProvider,
                             private val mapper: LessonMapper,
                             private val jobManager: JobManager,
                             private val logger: FirebaseLogger) {

    private val departments = PublishRelay.create<Boolean>()
    private val teachers = PublishRelay.create<String>()
    private val schedule = PublishRelay.create<Teacher>()

    fun departments(): Observable<ArrayList<Item>> = departments
            .observeOn(Schedulers.io())
            .map { teacherInfoProvider.getDepartments() }
            .map { ArrayList(it) }
            .hide()

    fun teachers(): Observable<ArrayList<Item>> = teachers
            .observeOn(Schedulers.io())
            .map { teacherInfoProvider.getTeachers(it) }
            .map { ArrayList(it) }
            .hide()

    fun getDepartments() {
        departments.accept(true)
    }

    fun getTeachers(departmentId: String) {
        teachers.accept(departmentId)
    }

    fun connectionStateChanges(): Observable<Boolean> =
            connectionStateProvider.connectionStateChanges()

    fun loadSchedule(teacher: Teacher) {
        schedule.accept(teacher)
    }

    fun scheduleLoadingStatus(): Observable<Boolean>  = schedule
            .observeOn(Schedulers.io())
            .map(teacherNetworkScheduleProvider::getSchedule)
            .map(mapper::networkToDb)
            .map { scheduleRepository.saveLessons(it) }
            .map { true }
            .doOnNext {
                jobManager.launchUpdaterJob()
                logger.logTeacher()
            }
            .doOnError { FirebaseCrash.report(it) }

    fun saveUser(teacher: Teacher) = userInfoStorage.saveUser(teacher)
}