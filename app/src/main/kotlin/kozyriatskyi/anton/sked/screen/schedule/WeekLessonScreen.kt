package kozyriatskyi.anton.sked.screen.schedule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.data.pojo.LessonDb
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.screen.schedule.WeekLessonsViewModel.State
import kozyriatskyi.anton.sked.util.ValueCallback
import kozyriatskyi.anton.sked.util.batched
import kozyriatskyi.anton.sked.util.zipWith
import kozyriatskyi.anton.sked.week.WeekViewInteractor
import java.time.LocalDate
import javax.inject.Inject

@Composable
fun WeekLessonsScreen(
    dates: List<LocalDate>,
    onLessonClick: ValueCallback<LessonUi>,
    viewModel: WeekLessonsViewModel = viewModel()
) {
    viewModel.getLessons(dates)

    val state by viewModel.state.collectAsState()

    when (state) {
        is State.Data -> LessonsList(
            days = (state as State.Data).days,
            onClick = onLessonClick
        )
        State.Loading -> {
            // nothing
        }
    }
}

class WeekLessonsViewModel @Inject constructor(
    private val interactor: WeekViewInteractor,
    private val dayMapper: DayMapper
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    fun getLessons(dates: List<LocalDate>) {
        interactor.getLessons(dates.first(), dates.last())
            .flowOn(Dispatchers.IO)
            .map { lessons -> lessons.groupBy(LessonDb::date) }
            .zipWith(dates.asFlow()) { groupedLessons, date -> date to groupedLessons[date].orEmpty() }
            .map { (date, lessons) -> dayMapper.createUiModel(date, lessons) }
            .batched(dates.size)
            .flowOn(Dispatchers.Default)
            .onEach { _state.value = State.Data(it) }
            .launchIn(viewModelScope)
    }

    sealed class State {

        object Loading : State()

        data class Data(val days: List<DayUi>) : State()
    }
}