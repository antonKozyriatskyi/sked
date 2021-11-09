package kozyriatskyi.anton.sked.byday

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kozyriatskyi.anton.sked.common.AppConfigurationManager
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.onFirstEmit
import kozyriatskyi.anton.sked.util.zip
import moxy.InjectViewState
import java.time.LocalDate

@InjectViewState
class ByDayViewPresenter(
    private val interactor: ByDayViewInteractor,
    private val dateManipulator: DateManipulator,
    private val itemMapper: ByDayViewItemMapper,
    private val appConfigurationManager: AppConfigurationManager
) : BasePresenter<ByDayView>() {

    override fun onFirstViewAttach() {
        appConfigurationManager.configurationChanges
            .flowOn(Dispatchers.IO)
            .onEach { setupDays() }
            .launchIn(scope)
    }

    private suspend fun setupDays() {
        val today = dateManipulator.today()
        val todayIsWorkday = dateManipulator.isWorkday(today)
        val hasLessonsOnWeekends = hasLessonsOnWeekends(today)
        val showCurrentWeek = todayIsWorkday || hasLessonsOnWeekends

        val days = when {
            showCurrentWeek -> getDays(false, hasLessonsOnWeekends.not())
            else -> getDays(true, canFilterOutWeekends = true)
        }

        days
            .flowOn(Dispatchers.IO)
            .onFirstEmit { checkPosition(it, today, showCurrentWeek) }
            .collect { viewState.showDays(it) }
    }

    private suspend fun hasLessonsOnWeekends(date: LocalDate): Boolean =
        dateManipulator.getRemainingWeekends(date)
            .any { interactor.hasLessonsOnDate(it).take(1).single() }

    @OptIn(FlowPreview::class)
    private fun getDays(
        nextWeek: Boolean,
        canFilterOutWeekends: Boolean
    ): Flow<List<ByDayViewItem>> {
        val weekNum = if (nextWeek) 1 else 0

        return dateManipulator.getWeekRange(weekNum)
            .map { date -> interactor.hasLessonsOnDate(date).map { date to it } }
            .zip()
            .map { data ->
                data.filter { (date, hasLessons) ->
                    dateManipulator.isWorkday(date) || (hasLessons || canFilterOutWeekends.not())
                }
                    .map(Pair<LocalDate, Boolean>::first)
            }
            .map { dates -> dates.map(itemMapper::create) }

    }

    private fun checkPosition(
        items: List<ByDayViewItem>,
        today: LocalDate,
        showCurrentWeek: Boolean
    ) = scope.launch {
        val position = when {
            showCurrentWeek -> items.indexOfFirst { it.date == today }
            else -> 0
        }

        viewState.showDayAt(position)
    }
}