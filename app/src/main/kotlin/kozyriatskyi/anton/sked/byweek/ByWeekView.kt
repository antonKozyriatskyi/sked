package kozyriatskyi.anton.sked.byweek

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ByWeekView : MvpView {

    fun showWeeks(dateTitles: Array<String>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNextWeek()
}