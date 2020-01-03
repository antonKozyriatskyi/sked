package kozyriatskyi.anton.sked.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class MainPresenter(private val userInfoStorage: UserInfoStorage,
                    private val userSettingsStorage: UserSettingsStorage,
                    private val interactor: MainInteractor) : MvpPresenter<MainView>() {

    private var scheduleUpdateDisposable: Disposable? = null

    override fun onFirstViewAttach() {
        viewState.setSubtitle(userInfoStorage.getUserName())

        when (userSettingsStorage.getInt(UserSettingsStorage.KEY_DEFAULT_VIEW_MODE, UserSettingsStorage.VIEW_BY_DAY)) {
            UserSettingsStorage.VIEW_BY_DAY -> viewState.setDayView()
            UserSettingsStorage.VIEW_BY_WEEK -> viewState.setWeekView()
            UserSettingsStorage.VIEW_TABLE -> viewState.setTableView()
        }
    }

    fun onSetDayViewClick() {
        viewState.setDayView()
    }

    fun onSetWeekViewClick() {
        viewState.setWeekView()
    }

    fun onSetTableViewClick() {
        viewState.setTableView()
    }

    fun onUpdateTriggered() {
        viewState.switchProgress(true)
        updateSchedule()
    }

    private fun updateSchedule() {
        scheduleUpdateDisposable = interactor.updateSchedule()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { viewState.switchProgress(false) }
                .subscribe({ viewState.onUpdateSucceeded() },
                        { viewState.onUpdateFailed() })
    }

    override fun onDestroy() {
        scheduleUpdateDisposable?.dispose()
    }
}