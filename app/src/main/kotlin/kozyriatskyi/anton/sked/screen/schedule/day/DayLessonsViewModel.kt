package kozyriatskyi.anton.sked.screen.schedule.day

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.day.DayViewInteractor
import kozyriatskyi.anton.sked.screen.schedule.day.DayLessonsViewModel.State.*
import java.time.LocalDate
import javax.inject.Inject

class DayLessonsViewModel constructor(
    private val interactor: DayViewInteractor,
    private val dayMapper: DayMapper
) : ViewModel() {

    private val _state = MutableStateFlow<State>(Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    fun getLessons(date: LocalDate) {
        interactor.observeLessons(date)
            .map { dayMapper.createUiModel(date, it) }
            .flowOn(Dispatchers.IO)
            .onEach {
                _state.value = when {
                    it.lessons.isEmpty() -> Empty(it)
                    else -> Data(it)
                }
            }
            .launchIn(viewModelScope)
    }

    sealed class State {

        object Loading : State()

        data class Empty(val day: DayUi) : State()

        data class Data(val day: DayUi) : State()
    }
}