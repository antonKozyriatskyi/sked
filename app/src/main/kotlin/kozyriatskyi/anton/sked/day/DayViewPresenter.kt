package kozyriatskyi.anton.sked.day

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.data.pojo.User

@InjectViewState
class DayViewPresenter(private val dayNumber: Int, private val nextWeek: Boolean,
                       private val interactor: DayViewInteractor, private val dayMapper: DayMapper) :
        MvpPresenter<DayView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        subscribeLessons()
    }

    fun onLessonClick(lesson: LessonUi) {
        val type = interactor.getUser().type
        if (type == User.Type.TEACHER) {
            viewState.showTeacherLessonDetails(lesson)
        } else if (type == User.Type.STUDENT) {
            viewState.showStudentLessonDetails(lesson)
        }
    }

    private fun subscribeLessons() {
        val weekNum = if (nextWeek) 1 else 0
        val disposable = interactor.lessons(dayNumber, weekNum)
                .map(dayMapper::dbToUi)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    viewState.showError(it.message ?: "Error")
                }
                .retry()
                .subscribe({
                    viewState.showDay(it)
                }, {
                    viewState.showError(it.message ?: "Error")
                })

        disposables.add(disposable)
    }

    override fun onDestroy() {
        disposables.clear()
    }
}