package kozyriatskyi.anton.sked.audiences

import kozyriatskyi.anton.sked.repository.Time
import kozyriatskyi.anton.sked.util.AddToEndSingleByTagStateStrategy
import moxy.MvpView
import moxy.viewstate.strategy.StateStrategyType

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