package kozyriatskyi.anton.sked.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.login.student.StudentLoginFragment
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginFragment
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class LoginFragment : MvpAppCompatFragment(R.layout.fragment_login), LoginView,
    OnInternetConnectionChangeListener,
    OnLoadingStateChangeListener {

    companion object {
        fun createArgs(userType: LoginView.UserType): Bundle {
            return bundleOf(EXTRA_USER_TYPE to userType)
        }

        private const val EXTRA_USER_TYPE = "mode"
    }

    private lateinit var toolbar: Toolbar

    private lateinit var onLoadButtonClickListener: OnLoadButtonClickListener

    private lateinit var loadButton: Button

    private lateinit var snackBar: Snackbar

    @Inject
    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun providePresenter(): LoginPresenter {
        val userType = requireArguments().getSerializable(EXTRA_USER_TYPE) as? LoginView.UserType
            ?: throw NullPointerException("userType must not be null")

        Injector.inject(this, userType)

        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupFragmentListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.login_toolbar)
        loadButton = view.findViewById(R.id.button_login_next)

        snackBar =
            Snackbar.make(loadButton, R.string.login_no_connection, Snackbar.LENGTH_INDEFINITE)

        toolbar.setNavigationOnClickListener {
            presenter.onBackClick()
        }

        view.findViewById<Button>(R.id.button_login_next).setOnClickListener {
            onLoadButtonClickListener.onLoadButtonClick()
        }
    }

    override fun onInternetConnectionStateChanged(isAvailable: Boolean) {
        presenter.onInternetConnectionStateChanged(isAvailable)
    }

    override fun onLoadingStateChanged(isLoaded: Boolean) {
        presenter.onLoadingStateChanged(isLoaded)
    }

    override fun switchNoConnectionMessage(show: Boolean) {
        if (show) {
            snackBar.show()
        } else {
            snackBar.dismiss()
        }
    }

    override fun enableUi(enable: Boolean) {
        loadButton.isEnabled = enable
    }

    override fun setTitle(title: String) {
        toolbar.title = title
    }

    override fun showStudentLayout() {
        childFragmentManager.beginTransaction()
            .add(R.id.login_layout_container, StudentLoginFragment(), StudentLoginFragment.TAG)
            .commit()
    }

    override fun showTeacherLayout() {
        childFragmentManager.beginTransaction()
            .add(R.id.login_layout_container, TeacherLoginFragment(), TeacherLoginFragment.TAG)
            .commit()
    }

    private fun setupFragmentListener() {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            if (fragment is OnLoadButtonClickListener) {
                onLoadButtonClickListener = fragment
            } else {
                throw IllegalArgumentException("fragment $fragment must implement OnLoadButtonClickListener")
            }
        }
    }

    interface OnLoadButtonClickListener {
        fun onLoadButtonClick()
    }
}
