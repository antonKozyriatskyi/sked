package kozyriatskyi.anton.sked.main

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Anton on 01.08.2017.
 */

@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSubtitle(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun switchProgress(showProgressBar: Boolean)

    fun setDayView()
    fun setWeekView()

    fun onUpdateFailed()
    fun onUpdateSucceeded()

    fun checkNotificationPermission()
    fun requestNotificationPermission()
}