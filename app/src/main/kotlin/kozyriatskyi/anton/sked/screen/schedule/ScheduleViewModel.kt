package kozyriatskyi.anton.sked.screen.schedule

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    private val userInfoStorage: UserInfoStorage,
    private val userSettingsStorage: UserSettingsStorage,
    private val interactor: ScheduleInteractor
) : ViewModel() {

    private var scheduleUpdateJob: Job? = null

    private val _state = MutableStateFlow(ScheduleScreenState())
    val state: StateFlow<ScheduleScreenState> get() = _state.asStateFlow()

    private val _updateStateResult = MutableSharedFlow<Boolean>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val updateStateResult: SharedFlow<Boolean> get() = _updateStateResult.asSharedFlow()

    fun onFirstViewAttach() {
        val username = userInfoStorage.getUserName()

        val viewMode = when (
            userSettingsStorage.getInt(
                UserSettingsStorage.KEY_DEFAULT_VIEW_MODE,
                UserSettingsStorage.VIEW_BY_DAY
            )
        ) {
            UserSettingsStorage.VIEW_BY_DAY -> ScheduleScreenViewMode.Day
            else -> ScheduleScreenViewMode.Week
        }

        updateState {
            copy(
                username = username,
                viewMode = viewMode
            )
        }

        observeUserSettings()
    }

    fun showDayView() {
        updateState { copy(viewMode = ScheduleScreenViewMode.Day) }
    }

    fun showWeekView() {
        updateState { copy(viewMode = ScheduleScreenViewMode.Week) }
    }

    fun updateSchedule() {
        scheduleUpdateJob?.cancel()

        updateState {
            copy(showProgress = true)
        }

        scheduleUpdateJob = viewModelScope.launch {
            val successfullyUpdated = withContext(Dispatchers.IO) {
                interactor.updateSchedule()
            }.isSuccess

            updateState {
                copy(showProgress = false)
            }

            _updateStateResult.emit(successfullyUpdated)
        }
    }

    private fun observeUserSettings() = viewModelScope.launch {
        userSettingsStorage.observeFirstDayOfWeek()
            .flowOn(Dispatchers.IO)
            .collect {
                interactor.updateFirstDayOfWeekMode(it)
            }
    }

    private fun updateState(action: ScheduleScreenState.() -> ScheduleScreenState) {
        _state.value = _state.value.action()
    }
}

@Immutable
data class ScheduleScreenState(
    val username: String? = null,
    val showProgress: Boolean = false,
    val viewMode: ScheduleScreenViewMode = ScheduleScreenViewMode.Day
)

enum class ScheduleScreenViewMode {
    Day, Week
}