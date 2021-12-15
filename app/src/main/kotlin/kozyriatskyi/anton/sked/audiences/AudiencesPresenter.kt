package kozyriatskyi.anton.sked.audiences

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.navigation.Navigator
import kozyriatskyi.anton.sked.repository.Time
import moxy.InjectViewState

@InjectViewState
class AudiencesPresenter(
    private val interactor: AudiencesInteractor,
    private val mapper: AudiencesMapper,
    private val navigator: Navigator
) : BasePresenter<AudiencesView>() {

    private var audiencesJob: Job? = null
    private var timesJob: Job? = null

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

        audiencesJob?.cancel()
        audiencesJob = scope.launch {
            withContext(Dispatchers.IO) {
                interactor.getAudiences(date, lessonStart.id, lessonEnd.id)
                    .map(mapper::toUi)
            }
                .onSuccess(::onAudiencesLoaded)
                .onFailure(::onErrorLoadingAudiences)
        }
    }

    private fun onAudiencesLoaded(audiences: List<AudienceUi>) {
        viewState.showAudiences(audiences)
    }

    private fun onErrorLoadingAudiences(t: Throwable) {
        viewState.showErrorLoadingAudiences()
    }

    private fun loadTimes() {
        viewState.showTimesLoading()

        timesJob?.cancel()
        timesJob = scope.launch {
            withContext(Dispatchers.IO) {
                interactor.getTimes()
                    .map { (start, end) ->
                        // sort times
                        val startSorted = start.sortedBy { it.id.toInt() }
                        val endSorted = end.sortedBy { it.id.toInt() }
                        Pair(startSorted, endSorted)
                    }
            }
                .onSuccess(::onTimesLoaded)
                .onFailure(::onErrorLoadingTimes)
        }
    }

    private fun onTimesLoaded(times: Pair<List<Time>, List<Time>>) {
        viewState.showTimes(times.first, times.second)
    }

    private fun onErrorLoadingTimes(t: Throwable) {
        viewState.showErrorLoadingTimes()
    }

    override fun onDestroy() {
        super.onDestroy()

        audiencesJob?.cancel()
        timesJob?.cancel()
    }

    fun onNavigateUpClicked() {
        navigator.pop()
    }
}