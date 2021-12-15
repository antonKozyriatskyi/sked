package kozyriatskyi.anton.sked.login.teacher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.data.pojo.Teacher
import kozyriatskyi.anton.sked.navigation.Destination
import kozyriatskyi.anton.sked.navigation.Navigator
import moxy.InjectViewState

/**
 * Created by Anton on 07.07.2017.
 */

@InjectViewState
class TeacherLoginPresenter(
    private val interactor: TeacherLoginInteractor,
    private val navigator: Navigator
) : BasePresenter<TeacherLoginView>() {

    private val uiModel = TeacherLoginStateModel()

    private val teacher = Teacher()

    private var retryAction: (() -> Unit)? = null

    override fun onFirstViewAttach() {
        observeConnectionState()

        loadDepartments()
    }

    override fun attachView(view: TeacherLoginView) {
        super.attachView(view)

        viewState.restorePositions(uiModel.departmentPosition, uiModel.teacherPosition)
    }

    fun onDepartmentChosen(department: String, departmentId: String, position: Int) {
        uiModel.departmentPosition = position

        teacher.department = department
        teacher.departmentId = departmentId

        loadTeachers(departmentId)
    }

    fun onTeacherChosen(teacher: String, teacherId: String, position: Int) {
        uiModel.teacherPosition = position

        this.teacher.teacher = teacher
        this.teacher.teacherId = teacherId

        setLoadedState()
    }

    fun retry() {
        retryAction?.let {
            viewState.switchError(show = false)
            it()
        }
    }

    fun onLoadButtonClick() {
        setLoadingState()
        retryAction = { loadSchedule(teacher) }
        loadSchedule(teacher)
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

    private fun loadDepartments() {
        setLoadingState()
        retryAction = { loadDepartments() }

        scope.launch {
            withContext(Dispatchers.IO) { interactor.loadDepartments() }
                .onSuccess { departments ->
                    viewState.showDepartments(departments)
                    if (departments.isEmpty()) {
                        viewState.showTeachers(emptyList())
                        setNotLoadedState()
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        viewState.switchError(
                            TeacherLoginFragment.ERROR_DEPARTMENTS,
                            "${it.message}",
                            true
                        )
                    }
                }
        }
    }

    private fun loadTeachers(departmentId: String) {
        setLoadingState()
        retryAction = { loadTeachers(departmentId) }

        scope.launch {
            withContext(Dispatchers.IO) { interactor.loadTeachers(departmentId) }
                .onSuccess { teachers ->
                    viewState.showTeachers(teachers)
                    if (teachers.isEmpty()) {
                        setNotLoadedState()
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        viewState.switchError(
                            TeacherLoginFragment.ERROR_TEACHER,
                            "${it.message}",
                            true
                        )
                    }
                }
        }
    }

    private fun loadSchedule(teacher: Teacher) {
        setLoadingState()

        scope.launch {
            withContext(Dispatchers.IO) { interactor.loadSchedule(teacher) }
                .onSuccess { navigator.goTo(Destination.Schedule) }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState()
                        viewState.switchError(
                            TeacherLoginFragment.ERROR_SCHEDULE,
                            "${it.message}",
                            true
                        )
                    }
                }
        }
    }

    private fun observeConnectionState() {
        scope.launch {
            interactor.connectionStateChanges()
                .collectLatest { onConnectionStateChanged(it) }
        }
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