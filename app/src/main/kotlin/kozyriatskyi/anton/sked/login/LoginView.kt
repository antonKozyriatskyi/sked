package kozyriatskyi.anton.sked.login

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

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