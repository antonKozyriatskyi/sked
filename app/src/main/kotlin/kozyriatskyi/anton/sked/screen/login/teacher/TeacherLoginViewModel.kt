package kozyriatskyi.anton.sked.screen.login.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.data.pojo.Teacher
import javax.inject.Inject

/**
 * Created by Anton on 07.07.2017.
 */
class TeacherLoginViewModel @Inject constructor(
    private val interactor: TeacherLoginInteractor
) : ViewModel() {

    private val uiModel = TeacherLoginStateModel()

    private val teacher = Teacher()

    private val _state = MutableStateFlow(uiModel)
    val state: StateFlow<TeacherLoginStateModel> get() = _state

    private var retryAction: (() -> Unit)? = null

    init {
        observeConnectionState()
        loadDepartments()
    }

    fun selectDepartment(department: Item) {
        teacher.department = department.value
        teacher.departmentId = department.id

        updateState {
            copy(
                selectedDepartments = department
            )
        }

        loadTeachers(department.id)
    }

    fun selectTeacher(teacher: Item) {
        this.teacher.teacher = teacher.value
        this.teacher.teacherId = teacher.id

        updateState {
            copy(selectedTeacher = teacher)
        }

        setLoadedState()
    }

    fun loadSchedule() {
        retryAction = { interactor.loadSchedule(teacher) }
        setLoadingState()
        loadSchedule(teacher)
    }

    fun retry() {
        retryAction?.let {
            updateState { copy(error = null, isLoading = true) }
            it()
        }
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
        updateState {
            copy(
                isLoading = false,
                isLoaded = true,
                error = null
            )
        }
    }

    private fun setNotLoadedState() {
        updateState {
            copy(
                isLoading = false,
                isLoaded = false,
                error = null
            )
        }
    }

    private fun setErrorState(error: TeacherLoadingError) {
        updateState {
            copy(
                isLoading = false,
                isLoaded = false,
                error = error
            )
        }
    }

    private fun loadDepartments() {
        setLoadingState()
        retryAction = { loadDepartments() }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadDepartments() }
                .onSuccess { departments ->
                    updateState {
                        if (departments.isEmpty()) {
                            copy(
                                departments = null,
                                teachers = null
                            )
                        } else {
                            copy(
                                departments = departments
                            )
                        }
                    }

                    retryAction = null
                    setNotLoadedState()
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(TeacherLoadingError.Department)
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
                    updateState {
                        copy(
                            teachers = teachers
                        )
                    }
                    if (teachers.isEmpty()) {
                        setNotLoadedState()
                    }

                    retryAction = null
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(TeacherLoadingError.Teacher)
                    }
                }
        }
    }

    private fun loadSchedule(teacher: Teacher) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { interactor.loadSchedule(teacher) }
                .onSuccess {
                    updateState {
                        copy(scheduleLoaded = true)
                    }
                }
                .onFailure {
                    if (uiModel.isConnectionAvailable) {
                        setErrorState(TeacherLoadingError.Schedule)
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

    private fun updateState(action: TeacherLoginStateModel.() -> TeacherLoginStateModel) {
        _state.value = _state.value.action()
    }
}