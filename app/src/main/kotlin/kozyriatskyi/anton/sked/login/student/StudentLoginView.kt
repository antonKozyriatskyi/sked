package kozyriatskyi.anton.sked.login.student

import kozyriatskyi.anton.sked.data.pojo.Item
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface StudentLoginView : MvpView {

    fun showMessage(msg: String)

    fun showFaculties(faculties: List<Item>)

    fun showCourses(courses: List<Item>)

    fun showGroups(groups: List<Item>)

    fun switchError(type: Int = 0, message: String = "", show: Boolean)

    fun switchProgress(show: Boolean)

    fun restorePositions(facultyPosition: Int, coursePosition: Int, groupPosition: Int)

    fun enableUi(setEnabled: Boolean)

    fun setLoaded(isLoaded: Boolean)

    fun onConnectionChanged(isConnectionAvailableNow: Boolean)
}