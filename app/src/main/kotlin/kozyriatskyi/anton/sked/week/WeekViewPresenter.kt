package kozyriatskyi.anton.sked.week

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.data.pojo.*
import kozyriatskyi.anton.sked.util.batched
import kozyriatskyi.anton.sked.util.zipWith
import moxy.InjectViewState
import java.time.LocalDate

@InjectViewState
class WeekViewPresenter(
    private val dates: List<LocalDate>,
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
        lessonsJob = interactor.getLessons(dates.first(), dates.last())
            .flowOn(Dispatchers.IO)
            .map { lessons -> lessons.groupBy(LessonDb::date) }
            .zipWith(dates.asFlow()) { groupedLessons, date -> date to groupedLessons[date].orEmpty() }
            .map { (date, lessons) -> dayMapper.createUiModel(date, lessons) }
            .batched(dates.size)
            .flowOn(Dispatchers.Default)
            .catch { viewState.showError(it.message.orEmpty()) }
            .onEach(viewState::showLessons)
            .launchIn(scope)
    }
}