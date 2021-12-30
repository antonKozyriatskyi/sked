package kozyriatskyi.anton.sked.schedule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.navigation.Destination
import kozyriatskyi.anton.sked.navigation.Navigator
import kozyriatskyi.anton.sked.screen.schedule.ScheduleInteractor
import moxy.InjectViewState

@InjectViewState
class SchedulePresenter(
    private val userInfoStorage: UserInfoStorage,
    private val userSettingsStorage: UserSettingsStorage,
    private val interactor: ScheduleInteractor,
    private val navigator: Navigator
) : BasePresenter<ScheduleView>() {

    private var scheduleUpdateJob: Job? = null

    override fun onFirstViewAttach() {
        viewState.setSubtitle(userInfoStorage.getUserName())

        when (userSettingsStorage.getInt(
            UserSettingsStorage.KEY_DEFAULT_VIEW_MODE,
            UserSettingsStorage.VIEW_BY_DAY
        )) {
            UserSettingsStorage.VIEW_BY_DAY -> viewState.setDayView()
            UserSettingsStorage.VIEW_BY_WEEK -> viewState.setWeekView()
        }

        observeUserSettings()
    }

    fun onSetDayViewClick() {
        viewState.setDayView()
    }

    fun onSetWeekViewClick() {
        viewState.setWeekView()
    }

    fun onReloginClick() {
        navigator.goTo(Destination.Intro(true))
    }

    fun onSettingsClick() {
        navigator.goTo(Destination.Settings)
    }

    fun onAudiencesClick() {
        navigator.goTo(Destination.Audiences)
    }

    fun onAboutClick() {
        navigator.goTo(Destination.About)
    }

    fun onUpdateTriggered() {
        updateSchedule()
    }

    private fun updateSchedule() {
        scheduleUpdateJob?.cancel()

        viewState.switchProgress(true)

        scheduleUpdateJob = scope.launch {
            withContext(Dispatchers.IO) {
                interactor.updateSchedule()
            }
                .onSuccess { viewState.onUpdateSucceeded() }
                .onFailure { viewState.onUpdateFailed() }

            viewState.switchProgress(false)
        }
    }

    private fun observeUserSettings() = scope.launch {
        userSettingsStorage.observeFirstDayOfWeek()
            .flowOn(Dispatchers.IO)
            .collect {
                interactor.updateFirstDayOfWeekMode(it)
            }
    }
}