package kozyriatskyi.anton.sked.login.student

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.data.pojo.Student
import moxy.InjectViewState
import javax.inject.Inject

/**
 * Created by Anton on 07.07.2017.
 */

@InjectViewState
class StudentLoginPresenter @Inject constructor(private val interactor: StudentLoginInteractor) :
    BasePresenter<StudentLoginView>() {

    private val uiModel = StudentLoginStateModel()

    private val student = Student()

    private var retryAction: (suspend () -> Unit)? = null

    override fun onFirstViewAttach() {

        observeConnectionState()
        loadFaculties()
    }

    override fun attachView(view: StudentLoginView) {
        super.attachView(view)

        viewState.restorePositions(
            uiModel.facultyPosition,
            uiModel.coursePosition,
            uiModel.groupPosition
        )
    }

    fun onFacultyChosen(faculty: String, facultyId: String, position: Int) {
        uiModel.facultyPosition = position

        student.faculty = faculty
        student.facultyId = facultyId

        loadCourses(facultyId)
    }

    fun onCourseChosen(course: String, courseId: String, position: Int) {
        uiModel.coursePosition = position

        student.course = course
        student.courseId = courseId

        loadGroups(courseId)
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
            scope.launch { it() }
        }
    }

    fun onLoadButtonClick() {
        setLoadingState()
        retryAction = { interactor.loadSchedule(student) }
        loadSchedule(student)
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

    private fun loadFaculties() {
        setLoadingState()
        retryAction = { loadFaculties() }

        scope.launch {
            withContext(Dispatchers.IO) { interactor.loadFaculties() }
                .onSuccess { faculties ->
                    viewState.showFaculties(faculties)
                    if (faculties.isEmpty()) {
                        viewState.showCourses(emptyList())
                        viewState.showGroups(emptyList())
                        setNotLoadedState()
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        viewState.switchError(
                            StudentLoginFragment.ERROR_FACULTIES,
                            "${it.message}",
                            true
                        )
                    }
                }
        }
    }

    private fun loadCourses(facultyId: String) {
        setLoadingState()
        retryAction = { loadCourses(facultyId) }

        scope.launch {
            withContext(Dispatchers.IO) { interactor.loadCourses(facultyId) }
                .onSuccess { courses ->
                    viewState.showCourses(courses)
                    if (courses.isEmpty()) {
                        // since faculties is an empty list I can use it to remove items from
                        // groups adapter
                        viewState.showGroups(courses)
                        setNotLoadedState()
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        viewState.switchError(
                            StudentLoginFragment.ERROR_COURSES,
                            "${it.message}",
                            true
                        )
                    }
                }
        }
    }

    private fun loadGroups(courseId: String) {
        setLoadingState()
        retryAction = { loadGroups(courseId) }

        scope.launch {
            withContext(Dispatchers.IO) { interactor.loadGroups(student.facultyId, courseId) }
                .onSuccess { groups ->
                    viewState.showGroups(groups)
                    if (groups.isEmpty()) {
                        setNotLoadedState()
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        viewState.switchError(
                            StudentLoginFragment.ERROR_GROUPS,
                            "${it.message}",
                            true
                        )
                    }
                }
        }
    }

    private fun loadSchedule(student: Student) {
        scope.launch {
            withContext(Dispatchers.IO) { interactor.loadSchedule(student) }
                .onSuccess { viewState.openScheduleScreen() }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        viewState.switchError(
                            StudentLoginFragment.ERROR_SCHEDULE,
                            "${it.message}",
                            true
                        )
                    }
                }
        }
    }


    private fun observeConnectionState() {
        interactor.connectionStateChanges()
            .flowOn(Dispatchers.IO)
            .onEach { onConnectionStateChanged(it) }
            .launchIn(scope)
    }

    private fun onConnectionStateChanged(isConnectionAvailableNow: Boolean) {
        uiModel.isConnectionAvailable = isConnectionAvailableNow
        viewState.enableUi(uiModel.enableUi)
        viewState.switchProgress(uiModel.showProgress)
        if (isConnectionAvailableNow.not()) viewState.switchError(show = false)
        viewState.onConnectionChanged(isConnectionAvailableNow)

        if (isConnectionAvailableNow) retry()
    }
}