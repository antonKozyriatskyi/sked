package kozyriatskyi.anton.sked.audiences

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@InjectViewState
class AudiencesPresenter(private val interactor: AudiencesInteractor,
                         private val mapper: AudiencesMapper) : MvpPresenter<AudiencesView>() {

    private var audiencesDisposable: Disposable? = null
    private var timesDisposable: Disposable? = null

    override fun onFirstViewAttach() {
        loadTimes()
    }

    private fun loadTimes() {
        timesDisposable = interactor.getTimes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showTimes(it.first, it.second)
                }, {
                    viewState.showErrorLoadingTimes()
                })
    }

    fun onLoadButtonClick(date: String, lessonStart: Int, lessonEnd: Int) {
        audiencesDisposable = interactor.getAudiences(date, lessonStart, lessonEnd)
                .subscribeOn(Schedulers.io())
                .map(mapper::networkToUi)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewState::showAudiences, { viewState.showErrorLoadingAudiences() })
    }

    override fun onDestroy() {
        audiencesDisposable?.dispose()
        timesDisposable?.dispose()
    }
}