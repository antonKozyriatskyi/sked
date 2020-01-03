package kozyriatskyi.anton.sked.byday

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ByDayView : MvpView {

    //    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showDays(isNextWeek: Boolean)

    @StateStrategyType(AddToEndStrategy::class)
    fun addTab()

    @StateStrategyType(AddToEndStrategy::class)
    fun removeTab()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setTodayPosition(todayPosition: Int)
}