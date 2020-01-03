package kozyriatskyi.anton.sked.login.student

import kozyriatskyi.anton.sked.data.pojo.Item
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.util.*

@StateStrategyType(AddToEndSingleStrategy::class)
interface StudentLoginView : MvpView {

    fun showMessage(msg: String)

    fun showFaculties(faculties: ArrayList<Item>)

    fun showCourses(courses: ArrayList<Item>)

    fun showGroups(groups: ArrayList<Item>)

    fun switchError(type: Int = 0, message: String = "", show: Boolean)

    fun switchProgress(show: Boolean)

    fun restorePositions(facultyPosition: Int, coursePosition: Int, groupPosition: Int)

    fun enableUi(setEnabled: Boolean)

    fun openScheduleScreen()

    fun setLoaded(isLoaded: Boolean)

    fun onConnectionChanged(isConnectionAvailableNow: Boolean)
}