package kozyriatskyi.anton.sked.byweek

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.util.DateUtils
import moxy.InjectViewState
import java.util.*

@InjectViewState
class ByWeekViewPresenter(private val interactor: ByWeekViewInteractor) :
    BasePresenter<ByWeekView>() {

    override fun onFirstViewAttach() {
        subscribe()
    }

    private fun subscribe() {
        thisWeekendLessonsCount()
    }

    private fun thisWeekendLessonsCount() {
        interactor.firstWeekendLessonsCount()
            .take(1)
            .flowOn(Dispatchers.IO)
            .onEach { (hasLessonsOnSaturday, hasLessonsOnSunday) ->
                val showNextWeek = showNextWeek(hasLessonsOnSaturday, hasLessonsOnSunday)
                viewState.showWeeks(getDateTitles())
                if (showNextWeek) viewState.showNextWeek()
            }
            .launchIn(scope)
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
        return Array(5) {
            "${DateUtils.mondayDate(it, inShortFormat = true)} - ${
                DateUtils.sundayDate(
                    it,
                    inShortFormat = true
                )
            }"
        }
    }
}