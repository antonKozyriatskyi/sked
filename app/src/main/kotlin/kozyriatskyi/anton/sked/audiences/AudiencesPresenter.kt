package kozyriatskyi.anton.sked.audiences

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kozyriatskyi.anton.sked.repository.Time
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class AudiencesPresenter(private val interactor: AudiencesInteractor,
                         private val mapper: AudiencesMapper) : MvpPresenter<AudiencesView>() {

    private var audiencesDisposable: Disposable? = null
    private var timesDisposable: Disposable? = null

    private lateinit var latestDate: String
    private lateinit var latestTimeStart: Time
    private lateinit var latestTimeEnd: Time

    override fun onFirstViewAttach() {
        loadTimes()
    }

    fun onLoadAudiencesButtonClick(date: String, timeStart: Time, timeEnd: Time) {
        // saving everything to use while reloading
        latestDate = date
        latestTimeStart = timeStart
        latestTimeEnd = timeEnd

        loadAudiences(date, timeStart, timeEnd)
    }

    fun onRetryLoadingAudiencesButtonClick() {
        loadAudiences(latestDate, latestTimeStart, latestTimeEnd)
    }

    fun onRetryLoadingTimesButtonClick() {
        loadTimes()
    }

    private fun loadAudiences(date: String, lessonStart: Time, lessonEnd: Time) {
        viewState.showAudiencesLoading()

        audiencesDisposable?.dispose()
        audiencesDisposable = interactor.getAudiences(date, lessonStart.id, lessonEnd.id)
                .subscribeOn(Schedulers.io())
                .map(mapper::toUi)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onAudiencesLoaded, ::onErrorLoadingAudiences)
    }

    private fun onAudiencesLoaded(audiences: List<AudienceUi>) {
        viewState.showAudiences(audiences)
    }

    private fun onErrorLoadingAudiences(t: Throwable) {
        viewState.showErrorLoadingAudiences()
    }

    private fun loadTimes() {
        viewState.showTimesLoading()

        timesDisposable?.dispose()
        timesDisposable = interactor.getTimes()
                .subscribeOn(Schedulers.io())
                .map {
                    // sort times
                    val start = it.first.sortedBy { it.id.toInt() }
                    val end = it.second.sortedBy { it.id.toInt() }
                    Pair(start, end)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onTimesLoaded, ::onErrorLoadingTimes)
    }

    private fun onTimesLoaded(times: Pair<List<Time>, List<Time>>) {
        viewState.showTimes(times.first, times.second)
    }

    private fun onErrorLoadingTimes(t: Throwable) {
        viewState.showErrorLoadingTimes()
    }

    override fun onDestroy() {
        audiencesDisposable?.dispose()
        timesDisposable?.dispose()
    }
}