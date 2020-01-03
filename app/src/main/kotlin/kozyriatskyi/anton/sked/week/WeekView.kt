package kozyriatskyi.anton.sked.week

import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface WeekView : MvpView {

    fun showLessons(lessons: List<DayUi>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setPosition(position: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showStudentLessonDetails(lesson: LessonUi)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showTeacherLessonDetails(lesson: LessonUi)
}