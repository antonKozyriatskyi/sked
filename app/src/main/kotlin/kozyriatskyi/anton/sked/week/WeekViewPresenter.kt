package kozyriatskyi.anton.sked.week

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.data.pojo.*
import moxy.InjectViewState

@InjectViewState
class WeekViewPresenter(
    private val weekNumber: Int,
    private val interactor: WeekViewInteractor,
    private val dayMapper: DayMapper
) : BasePresenter<WeekView>() {

    private var lessonsJob: Job? = null

    override fun onFirstViewAttach() {
        observeLessons()
    }

    fun onLessonClick(lesson: LessonUi) {
        when (interactor.getUser()) {
            is Teacher -> viewState.showTeacherLessonDetails(lesson)
            is Student -> viewState.showStudentLessonDetails(lesson)
        }
    }

    private fun observeLessons() {
        lessonsJob = interactor.lessons(weekNumber)
            .map(::removeWeekendsIfNoLessons)
            .map(dayMapper::dbToUi)
            .flowOn(Dispatchers.IO)
            .catch { viewState.showError(it.message ?: "Error") }
            .onEach(viewState::showLessons)
            .launchIn(scope)
    }

    @Suppress("NAME_SHADOWING")
    private fun removeWeekendsIfNoLessons(days: List<Day>): List<Day> {
        val days = days.toMutableList()

        val sunday = days[days.lastIndex]
        val saturday = days[days.lastIndex - 1]

        if (sunday.lessons.isEmpty()) {
            days.remove(sunday)
        }

        if (saturday.lessons.isEmpty()) {
            days.remove(saturday)
        }

        return days
    }
}