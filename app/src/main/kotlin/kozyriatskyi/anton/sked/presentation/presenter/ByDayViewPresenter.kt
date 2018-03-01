package kozyriatskyi.anton.sked.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kozyriatskyi.anton.sked.domain.interactor.ByDayViewInteractor
import kozyriatskyi.anton.sked.presentation.view.ByDayView
import java.util.*

@InjectViewState
class ByDayViewPresenter(private val interactor: ByDayViewInteractor) : MvpPresenter<ByDayView>() {

    private var hasSundayTab = false
    private var hasSaturdayTab = false

    private var todayPositionIsSet = false

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        subscribe()
    }

    private fun subscribe() {
        thisWeekendLessonsCount()
    }

    private fun todayPosition(isNextWeek: Boolean): Int {
        if (isNextWeek) return 0

        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        return if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - 2
    }

    private fun thisWeekendLessonsCount() {
        val disposable = interactor.thisWeekendLessonsCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (hasLessonsOnSaturday, hasLessonsOnSunday) ->
                    val showNextWeek = showNextWeek(hasLessonsOnSaturday, hasLessonsOnSunday)
                    viewState.showDays(showNextWeek)
                    subscribeWeekendLessonsCount(showNextWeek)
                }

        disposables.add(disposable)
    }

    private fun subscribeWeekendLessonsCount(nextWeek: Boolean) {
        val disposable = interactor.weekendLessonsCount(nextWeek)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    (hasLessonsOnSaturday, hasLessonsOnSunday) ->
                    editWeekendTabsIfNeeded(hasLessonsOnSaturday, hasLessonsOnSunday)

                    if (todayPositionIsSet.not()) {
                        viewState.setTodayPosition(todayPosition(nextWeek))
                        todayPositionIsSet = true
                    }
                }

        disposables.add(disposable)
    }

    private fun showNextWeek(hasLessonsOnSaturday: Boolean, hasLessonsOnSunday: Boolean): Boolean {
        val c = Calendar.getInstance()
        c.firstDayOfWeek = Calendar.MONDAY

        val dayOfWeek = c[Calendar.DAY_OF_WEEK]

        if (dayOfWeek == Calendar.SUNDAY) {
            return hasLessonsOnSunday.not()
        }

        val isWeekendToday = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
        val hasLessonsOnWeekend = hasLessonsOnSaturday or hasLessonsOnSunday
        // if today is weekend and there are no lessons on Saturday or Sunday - show next week
        return isWeekendToday and hasLessonsOnWeekend.not()
    }

    private fun editWeekendTabsIfNeeded(hasLessonsOnSaturday: Boolean, hasLessonsOnSunday: Boolean) {
        if (hasLessonsOnSaturday) {
            if (hasSaturdayTab.not()) {
                viewState.addTab()
                hasSaturdayTab = true
            }
        } else {
            if (hasSaturdayTab and hasLessonsOnSunday.not()) {
                viewState.removeTab()
                hasSaturdayTab = false
            }
        }

        if (hasLessonsOnSunday) {
            if (hasSaturdayTab.not()) {
                viewState.addTab()
                hasSaturdayTab = true
            }
            if (hasSundayTab.not()) {
                viewState.addTab()
                hasSundayTab = true
            }
        } else {
            if (hasSundayTab) {
                viewState.removeTab()
                hasSundayTab = false
            }

            if (hasLessonsOnSaturday.not() and hasSaturdayTab) viewState.removeTab()
        }
    }

    override fun onDestroy() {
        disposables.dispose()
    }
}