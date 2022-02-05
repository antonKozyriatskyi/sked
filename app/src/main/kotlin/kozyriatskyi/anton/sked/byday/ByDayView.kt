package kozyriatskyi.anton.sked.byday

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ByDayView : MvpView {

    fun showDays(days: List<ByDayViewItem>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showDayAt(todayPosition: Int)

}