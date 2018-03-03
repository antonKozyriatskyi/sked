package kozyriatskyi.anton.sked.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Created by Anton on 01.08.2017.
 */

@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSubtitle(text: String)

    fun switchProgress(showProgressBar: Boolean)

    fun setDayView()
    fun setWeekView()
    fun setTableView()
    fun onUpdateFailed()
    fun onUpdateSucceeded()
}