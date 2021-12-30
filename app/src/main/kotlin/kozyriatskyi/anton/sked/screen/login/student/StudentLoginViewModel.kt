package kozyriatskyi.anton.sked.screen.login.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.Student
import javax.inject.Inject

/**
 * Created by Anton on 07.07.2017.
 */
class StudentLoginViewModel @Inject constructor(private val interactor: StudentLoginInteractor) : ViewModel() {

    private val uiModel = StudentLoginStateModel()

    private val student = Student()

    private val _state = MutableStateFlow(uiModel)
    val state: StateFlow<StudentLoginStateModel>  get() = _state

    private var retryAction: (() -> Unit)? = null

    init {
        observeConnectionState()
        loadFaculties()
    }

    fun selectFaculty(faculty: Item) {
        student.faculty = faculty.value
        student.facultyId = faculty.id

        updateState {
            copy(
                selectedFaculty = faculty
            )
        }

        loadCourses(faculty.id)
    }

    fun selectCourse(course: Item) {
        student.course = course.value
        student.courseId = course.id

        updateState {
            copy(selectedCourse = selectedCourse)
        }

        loadGroups(course.id)
    }

    fun selectGroup(group: Item) {
        student.group = group.value
        student.groupId = group.id

        updateState {
            copy(selectedGroup = selectedGroup)
        }

        setLoadedState()
    }

    fun retry() {
        retryAction?.let {
            updateState { copy(error = null, isLoading = true) }

            it()
        }
    }

    fun loadSchedule() {
        retryAction = { loadSchedule() }
        setLoadingState()
        loadSchedule(student)
    }

    private fun setLoadingState() {
        updateState {
            copy(
                isLoading = true,
                isLoaded = false,
                error = null
            )
        }
    }

    private fun setLoadedState() {
        _state.value = _state.value.copy(
            isLoading = false,
            isLoaded = true,
            error = null
        )
    }

    private fun setNotLoadedState() {
        _state.value = _state.value.copy(
            isLoading = false,
            isLoaded = false,
            error = null
        )
    }

    private fun setErrorState(error: StudentLoadingError) {
        _state.value = _state.value.copy(
            isLoading = false,
            isLoaded = false,
            error = error
        )
    }

    private fun loadFaculties() {
        setLoadingState()
        retryAction = { loadFaculties() }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadFaculties() }
                .onSuccess { faculties ->
                    updateState {
                        if (faculties.isEmpty()) {
                            copy(
                                faculties = null,
                                courses = null,
                                groups = null
                            )
                        } else {
                            copy(
                                faculties = faculties
                            )
                        }
                    }

                    retryAction = null
                    setNotLoadedState()
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(StudentLoadingError.Faculties)
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
                    updateState {
                        if (courses.isEmpty()) {
                            copy(
                                courses = null,
                                groups = null
                            )
                        } else {
                            copy(
                                courses = courses
                            )
                        }
                    }

                    retryAction = null
                    setNotLoadedState()
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(StudentLoadingError.Courses)
                    }
                }
        }
    }

    private fun loadGroups(courseId: String) {
        setLoadingState()
        retryAction = { loadGroups(courseId) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadGroups(courseId) }
                .onSuccess { groups ->
                    updateState {
                        copy(groups = groups)
                    }
                    setNotLoadedState()
                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(StudentLoadingError.Groups)
                    }
                }
        }
    }

    private fun loadSchedule(student: Student) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadSchedule(student) }
                .onSuccess {
                    updateState {
                        copy(scheduleLoaded = true)
                    }
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(StudentLoadingError.Schedule)
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
        updateState {
            copy(isConnectionAvailable = isConnectionAvailableNow)
        }

        if (isConnectionAvailableNow) {
            retry()
        }
    }

    private fun updateState(action: StudentLoginStateModel.() -> StudentLoginStateModel) {
        _state.value = _state.value.action()
    }
}