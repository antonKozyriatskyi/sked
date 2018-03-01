package kozyriatskyi.anton.sked.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

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