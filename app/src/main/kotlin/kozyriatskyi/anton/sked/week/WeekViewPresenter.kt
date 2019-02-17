package kozyriatskyi.anton.sked.week

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kozyriatskyi.anton.sked.data.pojo.*
import kozyriatskyi.anton.sked.util.logD
import java.util.*

@InjectViewState
class WeekViewPresenter(private val weekNumber: Int,
                        private val interactor: WeekViewInteractor,
                        private val dayMapper: DayMapper)
    : MvpPresenter<WeekView>() {

    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        observeLessons()
    }

    fun onLessonClick(lesson: LessonUi) {
        val user = interactor.getUser()
        when (user) {
            is Teacher -> viewState.showTeacherLessonDetails(lesson)
            is Student -> viewState.showStudentLessonDetails(lesson)
        }
    }

    private fun observeLessons() {
        val disposable = interactor.lessons(weekNumber)
                .map(::removeWeekendsIfNoLessons)
                .map(dayMapper::dbToUi)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    logD("Error: $it")
                    viewState.showError(it.message ?: "Error")
                }
                .retry()
                .subscribe(viewState::showLessons)

        disposables.add(disposable)
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