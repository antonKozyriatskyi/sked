package kozyriatskyi.anton.sked.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import moxy.InjectViewState
import java.util.*

@InjectViewState
class MainPresenter(
    private val userInfoStorage: UserInfoStorage,
    private val userSettingsStorage: UserSettingsStorage,
    private val interactor: MainInteractor
) : BasePresenter<MainView>() {

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

        viewState.checkNotificationPermission()
    }

    fun updateLocale(locale: Locale) {
        interactor.updateLocale(locale)
    }

    fun onSetDayViewClick() {
        viewState.setDayView()
    }

    fun onSetWeekViewClick() {
        viewState.setWeekView()
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

    fun onNotificationPermissionChecked(hasPermission: Boolean) {
        if (hasPermission.not()) {
            viewState.requestNotificationPermission()
        }
    }
}