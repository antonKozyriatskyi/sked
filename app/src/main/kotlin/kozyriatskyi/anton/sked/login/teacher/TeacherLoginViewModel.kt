package kozyriatskyi.anton.sked.login.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.Teacher
import kozyriatskyi.anton.sked.util.MutableEventFlow
import kozyriatskyi.anton.sked.util.emit

class TeacherLoginViewModel(private val interactor: TeacherLoginInteractor) : ViewModel() {

    private val uiModel = TeacherLoginStateModel()

    private val teacher = Teacher()

    private var retryAction: (suspend () -> Unit)? = null

    private var _state: MutableStateFlow<TeacherLoginState> = MutableStateFlow(TeacherLoginState())
    val state: StateFlow<TeacherLoginState> = _state.asStateFlow()

    private val _openScheduleScreenEvent = MutableEventFlow<Unit>()
    val openScheduleScreenEvent: SharedFlow<Unit> = _openScheduleScreenEvent.asSharedFlow()

    private val _showConnectionError = MutableStateFlow(false)
    val showConnectionError: StateFlow<Boolean> = _showConnectionError.asStateFlow()

    init {
        observeConnectionState()
        loadDepartments()
    }

    fun chooseDepartment(department: Item) {
        teacher.department = department.value
        teacher.departmentId = department.id

        updateState {
            copy(
                selectedDepartment = department
            )
        }

        loadTeachers(department.id)
    }

    fun chooseTeacher(teacher: Item) {
        this.teacher.teacher = teacher.value
        this.teacher.teacherId = teacher.id

        updateState {
            copy(
                selectedTeacher = teacher
            )
        }

        setLoadedState()
    }

    fun retry() {
        retryAction?.let {
            _state.value = _state.value.copy(error = null)

            viewModelScope.launch { it() }
        }
    }

    fun loadSchedule() {
        setLoadingState()
        retryAction = { loadSchedule(teacher) }
        loadSchedule(teacher)
    }

    private fun setLoadingState(action: (TeacherLoginState.() -> TeacherLoginState)? = null) {
        uiModel.isLoading = true

        updateState {
            val state = copy(
                enableUi = it.enableUi,
                showLoading = it.showProgress,
                error = null,
                isLoaded = it.isLoaded
            )

            action?.invoke(state) ?: state
        }
    }

    private fun setLoadedState(action: (TeacherLoginState.() -> TeacherLoginState)? = null) {
        uiModel.isLoaded = true

        updateState {
            val state = copy(
                enableUi = it.enableUi,
                showLoading = it.showProgress,
                isLoaded = it.isLoaded
            )

            action?.invoke(state) ?: state
        }
    }

    private fun setNotLoadedState(action: (TeacherLoginState.() -> TeacherLoginState)? = null) {
        with(uiModel) {
            isError = false
            isLoading = false
            isLoaded = false
        }

        updateState {
            val state = copy(
                enableUi = it.enableUi,
                showLoading = it.showProgress,
                isLoaded = false
            )

            action?.invoke(state) ?: state
        }
    }

    private fun setErrorState(error: TeacherLoginError) {
        uiModel.isError = true

        updateState {
            copy(
                enableUi = it.enableUi,
                showLoading = it.showProgress,
                error = error,
                isLoaded = false
            )
        }
    }

    private fun loadDepartments() {
        setLoadingState()
        retryAction = { loadDepartments() }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadDepartments() }
                .onSuccess { departments ->
                    if (departments.isNotEmpty()) {
                        updateState {
                            copy(
                                departments = departments,
                                selectedDepartment = departments.first()
                            )
                        }

                        chooseDepartment(departments.first())
                    } else {
                        setNotLoadedState {
                            copy(
                                departments = emptyList(),
                                teachers = emptyList(),

                                selectedDepartment = null,
                                selectedTeacher = null,
                            )
                        }
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(
                            error = TeacherLoginError(
                                type = TeacherLoginErrorType.Departments,
                                message = it.message.orEmpty()
                            )
                        )
                    }
                }
        }
    }

    private fun loadTeachers(departmentId: String) {
        setLoadingState()
        retryAction = { loadTeachers(departmentId) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadTeachers(departmentId) }
                .onSuccess { teachers ->
                    if (teachers.isNotEmpty()) {
                        updateState {
                            copy(
                                teachers = teachers,
                                selectedTeacher = teachers.first()
                            )
                        }

                        chooseTeacher(teachers.first())
                    } else {
                        setNotLoadedState {
                            copy(
                                teachers = emptyList(),
                                selectedTeacher = null,
                            )
                        }
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(
                            error = TeacherLoginError(
                                type = TeacherLoginErrorType.Teachers,
                                message = it.message.orEmpty()
                            )
                        )
                    }
                }
        }
    }

    private fun loadSchedule(teacher: Teacher) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadSchedule(teacher) }
                .onSuccess { _openScheduleScreenEvent.emit() }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(
                            error = TeacherLoginError(
                                type = TeacherLoginErrorType.Schedule,
                                message = it.message.orEmpty()
                            )
                        )
                    }
                }
        }
    }


    private fun observeConnectionState() {
        interactor.connectionStateChanges()
            .flowOn(Dispatchers.IO)
            .onEach { onConnectionStateChanged(it) }
            .launchIn(viewModelScope)
    }

    private fun onConnectionStateChanged(isConnectionAvailableNow: Boolean) {
        uiModel.isConnectionAvailable = isConnectionAvailableNow

        updateState {
            copy(
                enableUi = it.enableUi,
                showLoading = it.showProgress,
                error = error.takeIf { isConnectionAvailableNow }
            )
        }

        _showConnectionError.tryEmit(isConnectionAvailableNow.not())

        if (isConnectionAvailableNow) retry()
    }

    private inline fun updateState(action: TeacherLoginState.(TeacherLoginStateModel) -> TeacherLoginState) {
        _state.value = _state.value.action(uiModel)
    }
}