package kozyriatskyi.anton.sked.login.student

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.Student
import kozyriatskyi.anton.sked.util.logE
import moxy.InjectViewState
import java.util.*
import javax.inject.Inject

/**
 * Created by Anton on 07.07.2017.
 */

@InjectViewState
class StudentLoginPresenter @Inject constructor(private val interactor: StudentLoginInteractor) : BasePresenter<StudentLoginView>() {

    private val disposables = CompositeDisposable()

    private val uiModel = StudentLoginStateModel()

    private val student = Student()

    private var retryAction: (() -> Unit)? = { setLoadingState(); interactor.getFaculties() }

    override fun onFirstViewAttach() {
        initSubscriptions()
    }

    fun onFacultyChosen(faculty: String, facultyId: String, position: Int) {
        uiModel.facultyPosition = position

        student.faculty = faculty
        student.facultyId = facultyId

        setLoadingState()
        retryAction = { interactor.getCourses(facultyId) }
        interactor.getCourses(facultyId)
    }

    fun onCourseChosen(course: String, courseId: String, position: Int) {
        uiModel.coursePosition = position

        student.course = course
        student.courseId = courseId

        setLoadingState()
        retryAction = { interactor.getGroups(courseId) }
        interactor.getGroups(courseId)
    }

    fun onGroupChosen(group: String, groupId: String, position: Int) {
        uiModel.groupPosition = position

        student.group = group
        student.groupId = groupId

        setLoadedState()
    }

    fun retry() {
        retryAction?.let {
            viewState.switchError(show = false)
            setLoadingState()
            it.invoke()
        }
    }

    fun onLoadButtonClick() {
        setLoadingState()
        retryAction = { interactor.loadSchedule(student) }
        interactor.loadSchedule(student)
    }

    private fun setLoadingState() {
        with(uiModel) {
            isLoading = true
            viewState.enableUi(enableUi)
            viewState.switchProgress(showProgress)
            viewState.switchError(show = false)
        }

        viewState.setLoaded(false)
    }

    private fun setLoadedState() {
        with(uiModel) {
            isLoaded = true
            viewState.enableUi(enableUi)
            viewState.switchProgress(showProgress)
        }

        viewState.setLoaded(true)
    }

    private fun setNotLoadedState() {
        with(uiModel) {
            isError = false
            isLoading = false
            isLoaded = false
            viewState.enableUi(enableUi)
            viewState.switchProgress(showProgress)
        }

        viewState.setLoaded(false)
    }

    private fun setErrorState() {
        with(uiModel) {
            isError = true
            viewState.enableUi(enableUi)
            viewState.switchProgress(showProgress)
            viewState.switchError(show = false)
        }

        viewState.setLoaded(false)
    }

    private fun subscribeSchedule() {
        disposables.add(interactor.scheduleLoadingStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    logE("Error loading student schedule: ${it.message}") //TODO
                    val str = "Error loading student schedule: ${student.faculty}[${student.facultyId}]\n${student.course}[${student.courseId}]\n${student.group}[${student.groupId}]\n"
                    FirebaseCrashlytics.getInstance().recordException(it)
                    FirebaseCrashlytics.getInstance().log(str)
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        viewState.switchError(StudentLoginFragment.ERROR_SCHEDULE, "${it.message}", true)
                    }
                }
                .retry()
                .subscribe {
                    interactor.saveUser(student)
                    viewState.openScheduleScreen()
                })
    }

    private fun subscribeFaculties() {
        disposables.add(interactor.faculties()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { retryAction = null }
                .subscribe({ faculties: ArrayList<Item> ->
                    viewState.showFaculties(faculties)
                    if (faculties.isEmpty()) {
                        // since faculties is an empty list I can use it to remove items from
                        // other adapters
                        viewState.showCourses(faculties)
                        viewState.showGroups(faculties)
                        setNotLoadedState()
                    }
                }, {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        logE("Error loading faculties: ${it.message}")
                        viewState.switchError(StudentLoginFragment.ERROR_FACULTIES, "${it.message}", true)
                    }
                    subscribeFaculties()
                }))
    }

    private fun subscribeCourses() {
        disposables.add(interactor.courses()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { retryAction = null }
                .subscribe({ courses: ArrayList<Item> ->
                    viewState.showCourses(courses)
                    if (courses.isEmpty()) {
                        // since faculties is an empty list I can use it to remove items from
                        // groups adapter
                        viewState.showGroups(courses)
                        setNotLoadedState()
                    }
                }, {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        viewState.switchError(StudentLoginFragment.ERROR_COURSES, "${it.message}", true)
                        logE("Error loading courses: ${it.message}")
                    }
                    subscribeCourses()
                }))
    }

    private fun subscribeGroups() {
        disposables.add(interactor.groups()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { retryAction = null }
                .subscribe({ groups: ArrayList<Item> ->
                    viewState.showGroups(groups)
                    if (groups.isEmpty()) {
                        setNotLoadedState()
                    }
                }, {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        logE("Error loading groups")
                        viewState.switchError(StudentLoginFragment.ERROR_GROUPS, "${it.message}", true)
                    }
                    subscribeGroups()
                }))
    }

    private fun subscribeConnectionState() {
        scope.launch {
            interactor.connectionStateChanges()
                .collectLatest { onConnectionStateChanged(it) }
        }
    }

    private fun initSubscriptions() {
        subscribeFaculties()
        subscribeCourses()
        subscribeGroups()
        subscribeSchedule()
        subscribeConnectionState()
    }

    private fun onConnectionStateChanged(isConnectionAvailableNow: Boolean) {
        uiModel.isConnectionAvailable = isConnectionAvailableNow
        viewState.enableUi(uiModel.enableUi)
        viewState.switchProgress(uiModel.showProgress)
        if (isConnectionAvailableNow.not()) viewState.switchError(show = false)
        viewState.onConnectionChanged(isConnectionAvailableNow)

        if (isConnectionAvailableNow) retry()
    }

    override fun attachView(view: StudentLoginView) {
        super.attachView(view)

        viewState.restorePositions(uiModel.facultyPosition, uiModel.coursePosition, uiModel.groupPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}