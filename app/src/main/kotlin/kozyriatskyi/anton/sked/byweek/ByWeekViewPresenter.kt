package kozyriatskyi.anton.sked.byweek

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.common.SCHEDULE_WEEKS_RANGE
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.combine
import kozyriatskyi.anton.sked.util.onFirstEmit
import moxy.InjectViewState
import java.time.LocalDate

@InjectViewState
class ByWeekViewPresenter(
    private val interactor: ByWeekViewInteractor,
    private val dateManipulator: DateManipulator,
    private val mapper: ByWeekViewItemMapper
) : BasePresenter<ByWeekView>() {

    override fun onFirstViewAttach() {
        setupWeeks()
    }

    private fun setupWeeks() = scope.launch {
        List(SCHEDULE_WEEKS_RANGE) {
            getItemForDate(dateManipulator.getWeekRange(it))
        }.combine()
            .onFirstEmit { checkPosition() }
            .collect {
                viewState.showWeekItems(it)
            }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun getItemForDate(dates: List<LocalDate>): Flow<ByWeekViewItem> {
        return flow { emit(mapper.create(dates)) }
    }

    private fun checkPosition() = scope.launch {
        val today = dateManipulator.today()

        val todayIsWorkday = dateManipulator.isWeekend(today).not()

        val hasLessonsOnWeekends = dateManipulator.getRemainingWeekends(today)
            .any { interactor.hasLessonsOnDate(it).take(1).single() }

        val startWeek = when {
            todayIsWorkday || hasLessonsOnWeekends -> 0
            else -> 1
        }
        viewState.showWeekAt(startWeek)
    }
}