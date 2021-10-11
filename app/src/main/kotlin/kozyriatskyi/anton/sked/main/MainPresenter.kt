package kozyriatskyi.anton.sked.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import moxy.InjectViewState

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
            val updateResult = withContext(Dispatchers.IO) {
                interactor.updateSchedule()
            }

            viewState.switchProgress(false)

            updateResult
                .onSuccess { viewState.onUpdateSucceeded() }
                .onFailure { viewState.onUpdateFailed() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        scheduleUpdateJob?.cancel()
    }
}