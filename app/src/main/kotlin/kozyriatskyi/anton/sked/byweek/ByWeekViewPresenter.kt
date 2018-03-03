package kozyriatskyi.anton.sked.byweek

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kozyriatskyi.anton.sked.util.DateUtils
import java.util.*

@InjectViewState
class ByWeekViewPresenter(private val interactor: ByWeekViewInteractor) : MvpPresenter<ByWeekView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        subscribe()
    }

    private fun subscribe() {
        thisWeekendLessonsCount()
    }

    private fun thisWeekendLessonsCount() {
        val disposable = interactor.firstWeekendLessonsCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (hasLessonsOnSaturday, hasLessonsOnSunday) ->
                    val showNextWeek = showNextWeek(hasLessonsOnSaturday, hasLessonsOnSunday)
                    viewState.showWeeks(getDateTitles())
                    if (showNextWeek) viewState.showNextWeek()
                }

        disposables.add(disposable)
    }

    private fun showNextWeek(hasLessonsOnSaturday: Boolean, hasLessonsOnSunday: Boolean): Boolean {
        val c = Calendar.getInstance()

        val dayOfWeek = c[Calendar.DAY_OF_WEEK]

        if (dayOfWeek == Calendar.SUNDAY) {
            return hasLessonsOnSunday.not()
        }

        val isWeekendToday = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
        val hasLessonsOnWeekend = hasLessonsOnSaturday or hasLessonsOnSunday
        // if today is weekend and there are no lessons on Saturday or Sunday - show next week
        return isWeekendToday and hasLessonsOnWeekend.not()
    }

    private fun getDateTitles(): Array<String> {
        return Array(5, {
            "${DateUtils.mondayDate(it, inShortFormat = true)} - ${DateUtils.sundayDate(it, inShortFormat = true)}"
        })
    }

    override fun onDestroy() {
        disposables.dispose()
    }
}