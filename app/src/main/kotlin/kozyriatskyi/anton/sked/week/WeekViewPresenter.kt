package kozyriatskyi.anton.sked.week

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.util.logD
import kozyriatskyi.anton.sked.util.logE
import java.util.*

@InjectViewState
class WeekViewPresenter(private val weekNumber: Int, private val interactor: WeekViewInteractor,
                        private val dayMapper: DayMapper) :
        MvpPresenter<WeekView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        subscribeLessons()
    }

    fun onLessonClick(lesson: LessonUi) {
        val type = interactor.getUser().type
        when (type) {
            User.Type.TEACHER -> viewState.showTeacherLessonDetails(lesson)
            User.Type.STUDENT -> viewState.showStudentLessonDetails(lesson)
        }
    }

    private fun subscribeLessons() {
        val disposable = interactor.lessons(weekNumber)
                .flatMap { Observable.fromIterable(it) }
                .map { dayMapper.dbToUi(it) }
                .buffer(7)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    logD("Error: $it")
                    viewState.showError(it.message ?: "Error")
                }
                .retry()
                .subscribe({
                    viewState.showLessons(it)
                }, {
                    viewState.showError(it.message ?: "Error")
                    logE("Week lessons error ${it.message}", it)
                }, {
                    logE("Week lessons complete")
                })

        disposables.add(disposable)
    }

    //TODO
    private fun todayPosition(isNextWeek: Boolean): Int {
        if (isNextWeek) return 0

        val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
        return if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - 2
    }

    override fun onDestroy() {
        disposables.clear()
    }
}