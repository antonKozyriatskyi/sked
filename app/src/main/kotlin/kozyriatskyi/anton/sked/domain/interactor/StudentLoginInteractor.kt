package kozyriatskyi.anton.sked.domain.interactor

import com.google.firebase.crash.FirebaseCrash
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.pojo.Student
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.domain.repository.ScheduleLoader
import kozyriatskyi.anton.sked.domain.repository.ScheduleStorage
import kozyriatskyi.anton.sked.domain.repository.StudentInfoLoader
import kozyriatskyi.anton.sked.util.FirebaseLogger
import kozyriatskyi.anton.sked.util.JobManager
import java.util.*

class StudentLoginInteractor(private val studentInfoProvider: StudentInfoLoader, // to get info from
                             private val userUserInfoStorage: UserInfoStorage, // to write info into
                             private val studentScheduleProvider: ScheduleLoader, // to get lessons from
                             private val scheduleRepository: ScheduleStorage, // to write lessons into
                             private val connectionStateProvider: ConnectionStateProvider,
                             private val mapper: LessonMapper,
                             private val jobManager: JobManager,
                             private val logger: FirebaseLogger) {

    private val faculties = PublishRelay.create<Boolean>()
    private val courses = PublishRelay.create<String>()
    private val groups = PublishRelay.create<String>()
    private val schedule = PublishRelay.create<Student>()

    fun faculties(): Observable<ArrayList<Item>> = faculties
            .observeOn(Schedulers.io())
            .map { studentInfoProvider.getFaculties() }
            .map { ArrayList(it) }
            .hide()

    fun courses(): Observable<ArrayList<Item>> = courses
            .observeOn(Schedulers.io())
            .map { studentInfoProvider.getCourses(it) }
            .map { ArrayList(it) }
            .hide()

    fun groups(): Observable<ArrayList<Item>> = groups
            .observeOn(Schedulers.io())
            .map { studentInfoProvider.getGroups(it) }
            .map { ArrayList(it) }
            .hide()

    fun getFaculties() = faculties.accept(true)

    fun getCourses(facultyId: String) = courses.accept(facultyId)

    fun getGroups(courseId: String) = groups.accept(courseId)

    fun connectionStateChanges(): Observable<Boolean> =
            connectionStateProvider.connectionStateChanges()

    fun loadSchedule(student: Student) = schedule.accept(student)

    fun scheduleLoadingStatus(): Observable<Boolean> = schedule
            .observeOn(Schedulers.io())
            .map(studentScheduleProvider::getSchedule)
            .map(mapper::networkToDb)
            .map(scheduleRepository::saveLessons)
            .map { true }
            .doOnNext {
                jobManager.launchUpdaterJob()
                logger.logStudent()
            }
            .doOnError { FirebaseCrash.report(it) }

    fun saveUser(student: Student) = userUserInfoStorage.saveUser(student)
}

