package kozyriatskyi.anton.sked.login.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.Student
import kozyriatskyi.anton.sked.util.MutableEventFlow
import kozyriatskyi.anton.sked.util.emit

class StudentLoginViewModel(private val interactor: StudentLoginInteractor) : ViewModel() {

    private val uiModel = StudentLoginStateModel()

    private val student = Student()

    private var retryAction: (suspend () -> Unit)? = null

    private var _state: MutableStateFlow<StudentLoginState> = MutableStateFlow(StudentLoginState())
    val state: StateFlow<StudentLoginState> = _state.asStateFlow()

    private val _openScheduleScreenEvent = MutableEventFlow<Unit>()
    val openScheduleScreenEvent: SharedFlow<Unit> = _openScheduleScreenEvent.asSharedFlow()

    private val _showConnectionError = MutableStateFlow(false)
    val showConnectionError: StateFlow<Boolean> = _showConnectionError.asStateFlow()

    init {
        observeConnectionState()
        loadFaculties()
    }

    fun chooseFaculty(faculty: Item) {
        student.faculty = faculty.value
        student.facultyId = faculty.id

        updateState {
            copy(
                selectedFaculty = faculty
            )
        }

        loadCourses(faculty.id)
    }

    fun chooseCourse(course: Item) {
        student.course = course.value
        student.courseId = course.id

        updateState {
            copy(
                selectedCourse = course
            )
        }

        loadGroups(course.id)
    }

    fun chooseGroup(group: Item) {
        student.group = group.value
        student.groupId = group.id

        updateState {
            copy(
                selectedGroup = group
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
        retryAction = { loadSchedule() }
        loadSchedule(student)
    }

    private fun setLoadingState(action: (StudentLoginState.() -> StudentLoginState)? = null) {
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

    private fun setLoadedState(action: (StudentLoginState.() -> StudentLoginState)? = null) {
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

    private fun setNotLoadedState(action: (StudentLoginState.() -> StudentLoginState)? = null) {
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

    private fun setErrorState(error: StudentLoginError) {
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

    private fun loadFaculties() {
        setLoadingState()
        retryAction = { loadFaculties() }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadFaculties() }
                .onSuccess { faculties ->
                    if (faculties.isNotEmpty()) {
                        updateState {
                            copy(
                                faculties = faculties,
                                selectedFaculty = faculties.first()
                            )
                        }

                        chooseFaculty(faculties.first())
                    } else {
                        setNotLoadedState {
                            copy(
                                faculties = emptyList(),
                                courses = emptyList(),
                                groups = emptyList(),

                                selectedFaculty = null,
                                selectedCourse = null,
                                selectedGroup = null,
                            )
                        }
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(
                            error = StudentLoginError(
                                type = StudentLoginErrorType.Faculties,
                                message = it.message.orEmpty()
                            )
                        )
                    }
                }
        }
    }

    private fun loadCourses(facultyId: String) {
        setLoadingState()
        retryAction = { loadCourses(facultyId) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadCourses(facultyId) }
                .onSuccess { courses ->
                    if (courses.isNotEmpty()) {
                        updateState {
                            copy(
                                courses = courses,
                                selectedCourse = courses.first()
                            )
                        }

                        chooseCourse(courses.first())
                    } else {
                        setNotLoadedState {
                            copy(
                                courses = emptyList(),
                                groups = emptyList(),

                                selectedCourse = null,
                                selectedGroup = null,
                            )
                        }
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(
                            error = StudentLoginError(
                                type = StudentLoginErrorType.Courses,
                                message = it.message.orEmpty()
                            )
                        )
                    }
                }
        }
    }

    private fun loadGroups(courseId: String) {
        setLoadingState()
        retryAction = { loadGroups(courseId) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadGroups(student.facultyId, courseId) }
                .onSuccess { groups ->
                    if (groups.isNotEmpty()) {
                        updateState {
                            copy(
                                groups = groups,
                                selectedGroup = groups.first()
                            )
                        }

                        chooseGroup(groups.first())
                    } else {
                        setNotLoadedState {
                            copy(
                                groups = emptyList(),
                                selectedGroup = null
                            )
                        }
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(
                            error = StudentLoginError(
                                type = StudentLoginErrorType.Groups,
                                message = it.message.orEmpty()
                            )
                        )
                    }
                }
        }
    }

    private fun loadSchedule(student: Student) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadSchedule(student) }
                .onSuccess { _openScheduleScreenEvent.emit() }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(
                            error = StudentLoginError(
                                type = StudentLoginErrorType.Schedule,
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

    private inline fun updateState(action: StudentLoginState.(StudentLoginStateModel) -> StudentLoginState) {
        _state.value = _state.value.action(uiModel)
    }
}