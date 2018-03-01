package kozyriatskyi.anton.sked.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ByWeekView : MvpView {

    fun showWeeks(dateTitles: Array<String>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNextWeek()
}