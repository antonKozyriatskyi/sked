package kozyriatskyi.anton.sked.audiences

import com.arellomobile.mvp.MvpView
import kozyriatskyi.anton.sked.data.pojo.Item

interface AudiencesView : MvpView {

    fun showAudiences(audiences: List<AudienceUi>)
    fun showErrorLoadingAudiences()

    fun showTimes(start: List<Item>, end: List<Item>)
    fun showErrorLoadingTimes()
}