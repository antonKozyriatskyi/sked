@file:OptIn(FlowPreview::class)

package kozyriatskyi.anton.sked.screen.schedule.byDay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kozyriatskyi.anton.sked.byday.ByDayViewInteractor
import kozyriatskyi.anton.sked.byday.ByDayViewItem
import kozyriatskyi.anton.sked.byday.ByDayViewItemMapper
import kozyriatskyi.anton.sked.common.AppConfigurationManager
import kozyriatskyi.anton.sked.data.pojo.LessonDb
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.batched
import kozyriatskyi.anton.sked.util.onFirstEmit
import kozyriatskyi.anton.sked.util.zip
import java.time.LocalDate
import javax.inject.Inject

class ByDayViewModel @Inject constructor(
    private val interactor: ByDayViewInteractor,
    private val dateManipulator: DateManipulator,
    private val itemMapper: ByDayViewItemMapper,
    private val appConfigurationManager: AppConfigurationManager
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _dayPosition = MutableSharedFlow<Int>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val dayPosition: SharedFlow<Int> = _dayPosition.asSharedFlow()

    init {
        observeAppConfigurationChanges()
    }

    private fun observeAppConfigurationChanges() {
        appConfigurationManager.configurationChanges
            .flowOn(Dispatchers.IO)
            .onEach { setupDays() }
            .launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private suspend fun setupDays() {
        val today = dateManipulator.today()
        val todayIsWorkday = dateManipulator.isWorkday(today)
        val hasLessonsOnWeekends = hasLessonsOnWeekends(today)
        val showCurrentWeek = todayIsWorkday || hasLessonsOnWeekends

        val weekDates = when {
            showCurrentWeek -> getDays(false, canFilterOutWeekends = hasLessonsOnWeekends.not())
            else -> getDays(true, canFilterOutWeekends = true)
        }

        weekDates
            .flatMapConcat { dates -> getLessonsByDay(dates).batched(dates.size) }
            .map { items -> items.map { (date, lessons) -> itemMapper.create(date, lessons) }  }
            .flowOn(Dispatchers.IO)
            .onFirstEmit { checkPosition(it, today, showCurrentWeek) }
            .collect { _state.value = State.Data(it) }
    }

    private suspend fun hasLessonsOnWeekends(date: LocalDate): Boolean =
        dateManipulator.getRemainingWeekends(date)
            .any { interactor.hasLessonsOnDate(it).take(1).single() }

    private fun getDays(
        nextWeek: Boolean,
        canFilterOutWeekends: Boolean
    ): Flow<List<LocalDate>> {
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

    }

    private fun getLessonsByDay(dates: List<LocalDate>): Flow<Pair<LocalDate, List<LessonDb>>> {
        return dates.asFlow()
            .flatMapConcat { date ->
                interactor.getLessons(date).map {
                    date to it
                }
            }
    }

    private fun checkPosition(
        items: List<ByDayViewItem>,
        today: LocalDate,
        showCurrentWeek: Boolean
    ) = viewModelScope.launch {
        val position = when {
            showCurrentWeek -> items.indexOfFirst { it.day.date == today }
            else -> 0
        }

        _dayPosition.emit(position)
    }

    sealed class State {
        object Loading : State()

        class Data(val items: List<ByDayViewItem>) : State()
    }
}