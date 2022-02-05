package kozyriatskyi.anton.sked.day

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.data.pojo.Student
import kozyriatskyi.anton.sked.data.pojo.Teacher
import moxy.InjectViewState
import java.time.LocalDate

@InjectViewState
class DayViewPresenter(
    private val date: LocalDate,
    private val interactor: DayViewInteractor,
    private val dayMapper: DayMapper
) : BasePresenter<DayView>() {

    private var lessonsJob: Job? = null

    override fun onFirstViewAttach() {
        observeLessons()
    }

    fun onLessonClick(lesson: LessonUi) {
        val user = interactor.getUser()
        if (user is Teacher) {
            viewState.showTeacherLessonDetails(lesson)
        } else if (user is Student) {
            viewState.showStudentLessonDetails(lesson)
        }
    }

    private fun observeLessons() {
        lessonsJob = interactor.observeLessons(date)
            .map { dayMapper.createUiModel(date, it) }
            .flowOn(Dispatchers.IO)
            .catch { viewState.showError(it.message.orEmpty()) }
            .onEach { viewState.showDay(it) }
            .launchIn(scope)
    }
}