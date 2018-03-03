package kozyriatskyi.anton.sked.day

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.data.pojo.LessonUi

@StateStrategyType(AddToEndSingleStrategy::class)
interface DayView : MvpView {

    fun showDay(day: DayUi)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showStudentLessonDetails(lesson: LessonUi)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showTeacherLessonDetails(lesson: LessonUi)
}