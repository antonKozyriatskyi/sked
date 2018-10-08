package kozyriatskyi.anton.sked.login.teacher

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Spinner
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.login.ItemSpinnerAdapter
import kozyriatskyi.anton.sked.login.LoginActivity
import kozyriatskyi.anton.sked.login.OnInternetConnectionChangeListener
import kozyriatskyi.anton.sked.login.OnLoadingStateChangeListener
import kozyriatskyi.anton.sked.main.MainActivity
import kozyriatskyi.anton.sked.util.*
import java.util.*
import javax.inject.Inject


class TeacherLoginFragment : MvpAppCompatFragment(), TeacherLoginView, AdapterView.OnItemSelectedListener,
        LoginActivity.OnLoadButtonClickListener {

    companion object {
        const val TAG = "TEACHER_FRAGMENT"

        const val ERROR_DEPARTMENTS = 0
        const val ERROR_TEACHER = 1
        const val ERROR_SCHEDULE = 2
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: TeacherLoginPresenter

    private lateinit var progressBar: ProgressBar
    private lateinit var departmentsSpinner: Spinner
    private lateinit var teachersSpinner: Spinner

    private lateinit var errorDialog: AlertDialog

    private lateinit var onInternetConnectionChangeListener: OnInternetConnectionChangeListener
    private lateinit var onLoadingStateChangeListener: OnLoadingStateChangeListener

    @Suppress("unused")
    @ProvidePresenter
    fun providePresenter(): TeacherLoginPresenter {
        Injector.teacherComponent().inject(this)
        return presenter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnInternetConnectionChangeListener) {
            onInternetConnectionChangeListener = context
        } else {
            throw IllegalArgumentException("context $context must implement OnInternetConnectionChangeListener")
        }

        if (context is OnLoadingStateChangeListener) {
            onLoadingStateChangeListener = context
        } else {
            throw IllegalArgumentException("context $context must implement OnLoadingStateChangeListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_teacher_login, container, false)

        progressBar = view.find<ProgressBar>(R.id.progress_login_teacher)

        departmentsSpinner = view.find<Spinner>(R.id.spinner_login_teacher_departments)
        teachersSpinner = view.find<Spinner>(R.id.spinner_login_teacher_teachers)

        departmentsSpinner.adapter = ItemSpinnerAdapter()
        teachersSpinner.adapter = ItemSpinnerAdapter()

        departmentsSpinner.onItemSelectedListener = this
        teachersSpinner.onItemSelectedListener = this

        errorDialog = AlertDialog.Builder(context!!)
                .setCancelable(false)
                .setNegativeButton(R.string.exit) { dialog, _ ->
                    dialog.dismiss()
                    activity!!.finish()
                }
                .setPositiveButton(R.string.retry) { dialog, _ ->
                    dialog.dismiss()
                    presenter.retry()
                }
                .create()

        return view
    }

    override fun switchProgress(show: Boolean) {
        if (show) progressBar.setVisible() else progressBar.setInvisible()
    }

    override fun switchError(type: Int, message: String, show: Boolean) {
        if (show) {
            showDialog(type, message)
        } else {
            if (errorDialog.isShowing) errorDialog.dismiss()
        }
    }

    override fun showMessage(msg: String) = toast(msg)

    private fun showDialog(type: Int, message: String) {
        val title = when (type) {
            ERROR_DEPARTMENTS -> getString(R.string.login_error_departments)
            ERROR_TEACHER -> getString(R.string.login_error_teachers)
            else -> getString(R.string.login_error_schedule)
        }

        errorDialog.setTitle(title)
        errorDialog.setMessage(message)
        errorDialog.show()
    }

    override fun showDepartments(departments: ArrayList<Item>) {
        (departmentsSpinner.adapter as ItemSpinnerAdapter).updateData(departments)
    }

    override fun showTeachers(teachers: ArrayList<Item>) {
        (teachersSpinner.adapter as ItemSpinnerAdapter).updateData(teachers)
    }

    override fun restorePositions(departmentPosition: Int, teacherPosition: Int) {
        departmentsSpinner.onItemSelectedListener = null
        teachersSpinner.onItemSelectedListener = null

        departmentsSpinner.setSelection(departmentPosition, false)
        teachersSpinner.setSelection(teacherPosition, false)

        departmentsSpinner.onItemSelectedListener = this
        teachersSpinner.onItemSelectedListener = this
    }

    override fun enableUi(setEnabled: Boolean) {
        if (setEnabled) {
            departmentsSpinner.setEnabled()
            teachersSpinner.setEnabled()
        } else {
            departmentsSpinner.setDisabled()
            teachersSpinner.setDisabled()
        }
    }

    override fun openScheduleScreen() {
        MainActivity.start(context!!)
        activity!!.finish()
    }

    override fun setLoaded(isLoaded: Boolean) {
        onLoadingStateChangeListener.onLoadingStateChanged(isLoaded)
    }

    override fun onConnectionChanged(isConnectionAvailableNow: Boolean) {
        onInternetConnectionChangeListener.onInternetConnectionStateChanged(isConnectionAvailableNow)
    }

    override fun onLoadButtonClick() {
        presenter.onLoadButtonClick()
    }

    /* Spinner callbacks start */
    override fun onNothingSelected(parent: AdapterView<*>?) {
        //ignore
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.let {
            when (it.id) {
                departmentsSpinner.id -> {
                    val department = (it.adapter as ItemSpinnerAdapter).getSelectedItem(position)
                    presenter.onDepartmentChosen(department.value, department.id, position)
                }
                teachersSpinner.id -> {
                    val teacher = (it.adapter as ItemSpinnerAdapter).getSelectedItem(position)
                    presenter.onTeacherChosen(teacher.value, teacher.id, position)
                }
            }
        }
    }
    /* Spinner callbacks end */

    override fun onDestroy() {
        super.onDestroy()
//        val refWatcher = App.getRefWatcher(activity)
//        refWatcher.watch(this)
    }
}