package kozyriatskyi.anton.sked.login

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Anton on 05.03.2017.
 */

@StateStrategyType(OneExecutionStateStrategy::class)
interface LoginView : MvpView {
    fun showStudentLayout()
    fun showTeacherLayout()
    fun enableUi(enable: Boolean)
    fun switchNoConnectionMessage(show: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setTitle(title: String)

    enum class UserType { STUDENT, TEACHER }
}