package kozyriatskyi.anton.sked.login.teacher

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import kozyriatskyi.anton.sked.data.pojo.Item
import java.util.*

@StateStrategyType(AddToEndSingleStrategy::class)
interface TeacherLoginView : MvpView {

    fun showMessage(msg: String)

    fun showDepartments(departments: ArrayList<Item>)

    fun showTeachers(teachers: ArrayList<Item>)

    fun switchError(type: Int = 0, message: String = "", show: Boolean)

    fun switchProgress(show: Boolean)

    fun restorePositions(departmentPosition: Int, teacherPosition: Int)

    fun enableUi(setEnabled: Boolean)

    fun openScheduleScreen()

    fun setLoaded(isLoaded: Boolean)

    fun onConnectionChanged(isConnectionAvailableNow: Boolean)
}