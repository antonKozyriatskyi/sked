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

@InjectViewState
class DayViewPresenter(
    private val dayNumber: Int, private val nextWeek: Boolean,
    private val interactor: DayViewInteractor, private val dayMapper: DayMapper
) :
    BasePresenter<DayView>() {

    private var lessonsJob: Job? = null

    override fun onFirstViewAttach() {
        subscribeLessons()
    }

    fun onLessonClick(lesson: LessonUi) {
        val user = interactor.getUser()
        if (user is Teacher) {
            viewState.showTeacherLessonDetails(lesson)
        } else if (user is Student) {
            viewState.showStudentLessonDetails(lesson)
        }
    }

    private fun subscribeLessons() {
        val weekNum = if (nextWeek) 1 else 0

        lessonsJob = interactor.lessons(dayNumber, weekNum)
            .map(dayMapper::dbToUi)
            .flowOn(Dispatchers.IO)
            .catch { viewState.showError(it.message ?: "Error") }
            .onEach { viewState.showDay(it) }
            .launchIn(scope)
    }
}