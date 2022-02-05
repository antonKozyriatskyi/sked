package kozyriatskyi.anton.sked.byweek

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ByWeekView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showWeekAt(position: Int)

    fun showWeekItems(weekItems: List<ByWeekViewItem>)
}