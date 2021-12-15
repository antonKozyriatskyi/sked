package kozyriatskyi.anton.sked.login.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.login.ItemSpinnerAdapter
import kozyriatskyi.anton.sked.login.LoginFragment
import kozyriatskyi.anton.sked.login.OnInternetConnectionChangeListener
import kozyriatskyi.anton.sked.login.OnLoadingStateChangeListener
import kozyriatskyi.anton.sked.util.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class StudentLoginFragment : MvpAppCompatFragment(), StudentLoginView, AdapterView.OnItemSelectedListener,
        LoginFragment.OnLoadButtonClickListener {

    companion object {
        const val TAG = "STUDENT_FRAGMENT"

        const val ERROR_FACULTIES = 0
        const val ERROR_COURSES = 1
        const val ERROR_GROUPS = 2
        const val ERROR_SCHEDULE = 3
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: StudentLoginPresenter

    private lateinit var progressBar: ProgressBar
    private lateinit var facultiesSpinner: Spinner
    private lateinit var coursesSpinner: Spinner
    private lateinit var groupsSpinner: Spinner

    private lateinit var errorDialog: AlertDialog

    private lateinit var onInternetConnectionChangeListener: OnInternetConnectionChangeListener
    private lateinit var onLoadingStateChangeListener: OnLoadingStateChangeListener

    @Suppress("unused")
    @ProvidePresenter
    fun providePresenter(): StudentLoginPresenter {
        Injector.inject(this)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val parent = parentFragment
        if (parent is OnInternetConnectionChangeListener) {
            onInternetConnectionChangeListener = parent
        } else {
            throw ClassCastException("parentFragment $parentFragment must implement OnInternetConnectionChangeListener")
        }

        if (parent is OnLoadingStateChangeListener) {
            onLoadingStateChangeListener = parent
        } else {
            throw ClassCastException("parentFragment $parentFragment must implement OnLoadingStateChangeListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_student_login, container, false)

        progressBar = view.find<ProgressBar>(R.id.progress_login_student)

        facultiesSpinner = view.find<Spinner>(R.id.spinner_login_student_faculties)
        coursesSpinner = view.find<Spinner>(R.id.spinner_login_student_courses)
        groupsSpinner = view.find<Spinner>(R.id.spinner_login_student_groups)

        facultiesSpinner.adapter = ItemSpinnerAdapter()
        coursesSpinner.adapter = ItemSpinnerAdapter()
        groupsSpinner.adapter = ItemSpinnerAdapter()

        facultiesSpinner.onItemSelectedListener = this
        coursesSpinner.onItemSelectedListener = this
        groupsSpinner.onItemSelectedListener = this

        errorDialog = AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setNegativeButton(R.string.exit) { dialog, _ ->
                    dialog.dismiss()
                    requireActivity().finish()
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
            ERROR_FACULTIES -> getString(R.string.login_error_faculties)
            ERROR_COURSES -> getString(R.string.login_error_courses)
            ERROR_GROUPS -> getString(R.string.login_error_groups)
            else -> getString(R.string.login_error_schedule)
        }

        errorDialog.setTitle(title)
        errorDialog.setMessage(message)
        errorDialog.show()
    }

    override fun showFaculties(faculties: List<Item>) {
        (facultiesSpinner.adapter as ItemSpinnerAdapter).updateData(faculties)
    }

    override fun showCourses(courses: List<Item>) {
        (coursesSpinner.adapter as ItemSpinnerAdapter).updateData(courses)
    }

    override fun showGroups(groups: List<Item>) {
        (groupsSpinner.adapter as ItemSpinnerAdapter).updateData(groups)
    }

    override fun restorePositions(facultyPosition: Int, coursePosition: Int, groupPosition: Int) {
        facultiesSpinner.onItemSelectedListener = null
        coursesSpinner.onItemSelectedListener = null
        groupsSpinner.onItemSelectedListener = null

        facultiesSpinner.setSelection(facultyPosition, false)
        coursesSpinner.setSelection(coursePosition, false)
        groupsSpinner.setSelection(groupPosition, false)

        facultiesSpinner.onItemSelectedListener = this
        coursesSpinner.onItemSelectedListener = this
        groupsSpinner.onItemSelectedListener = this
    }

    override fun enableUi(setEnabled: Boolean) {
        if (setEnabled) {
            facultiesSpinner.setEnabled()
            coursesSpinner.setEnabled()
            groupsSpinner.setEnabled()
        } else {
            facultiesSpinner.setDisabled()
            coursesSpinner.setDisabled()
            groupsSpinner.setDisabled()
        }
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
                facultiesSpinner.id -> {
                    val faculty = (it.adapter as ItemSpinnerAdapter).getSelectedItem(position)
                    presenter.onFacultyChosen(faculty.value, faculty.id, position)
                }
                coursesSpinner.id -> {
                    val course = (it.adapter as ItemSpinnerAdapter).getSelectedItem(position)
                    presenter.onCourseChosen(course.value, course.id, position)
                }
                groupsSpinner.id -> {
                    val group = (it.adapter as ItemSpinnerAdapter).getSelectedItem(position)
                    presenter.onGroupChosen(group.value, group.id, position)
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