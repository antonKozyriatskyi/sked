package kozyriatskyi.anton.sked.audiences

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import kozyriatskyi.anton.sked.repository.Time
import kozyriatskyi.anton.sked.util.AddToEndSingleByTagStateStrategy

private const val TAG_AUDIENCES = "audiences"
private const val TAG_TIMES = "times"

interface AudiencesView : MvpView {

    @StateStrategyType(AddToEndSingleByTagStateStrategy::class, tag = TAG_AUDIENCES)
    fun showAudiences(audiences: List<AudienceUi>)

    @StateStrategyType(AddToEndSingleByTagStateStrategy::class, tag = TAG_AUDIENCES)
    fun showErrorLoadingAudiences()

    @StateStrategyType(AddToEndSingleByTagStateStrategy::class, tag = TAG_AUDIENCES)
    fun showAudiencesLoading()

    @StateStrategyType(AddToEndSingleByTagStateStrategy::class, tag = TAG_TIMES)
    fun showTimes(start: List<Time>, end: List<Time>)

    @StateStrategyType(AddToEndSingleByTagStateStrategy::class, tag = TAG_TIMES)
    fun showErrorLoadingTimes()

    @StateStrategyType(AddToEndSingleByTagStateStrategy::class, tag = TAG_TIMES)
    fun showTimesLoading()
}