package kozyriatskyi.anton.sked.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.login.student.StudentLoginFragment
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginFragment
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class LoginActivity : MvpAppCompatActivity(), LoginView, OnInternetConnectionChangeListener,
        OnLoadingStateChangeListener {

    companion object {
        const val EXTRA_USER_TYPE = "mode"

        fun start(context: Context, userType: LoginView.UserType) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(EXTRA_USER_TYPE, userType)
            context.startActivity(intent)
        }
    }

    interface OnLoadButtonClickListener {
        fun onLoadButtonClick()
    }

    private lateinit var onLoadButtonClickListener: OnLoadButtonClickListener

    private lateinit var loadButton: Button

    private lateinit var snackBar: Snackbar

    @Inject
    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun providePresenter(): LoginPresenter {
        val userType = intent.getSerializableExtra(EXTRA_USER_TYPE) as? LoginView.UserType
                ?: throw NullPointerException("userType must not be null")
        Injector.inject(this, userType)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_ActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadButton = findViewById(R.id.button_login_next)

        snackBar = Snackbar.make(loadButton, R.string.login_no_connection, Snackbar.LENGTH_INDEFINITE)
    }

    fun onLoadScheduleClick(v: View) {
        onLoadButtonClickListener.onLoadButtonClick()
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
//        toolbarTitle = title
        supportActionBar?.title = title
//        supportActionBar?.title = toolbarTitle
    }

    override fun showStudentLayout() {
//        supportActionBar?.title = toolbarTitle
        supportFragmentManager.beginTransaction()
                .add(R.id.login_layout_container, StudentLoginFragment(), StudentLoginFragment.TAG)
                .commit()
    }

    override fun showTeacherLayout() {
//        supportActionBar?.title = toolbarTitle
        supportFragmentManager.beginTransaction()
                .add(R.id.login_layout_container, TeacherLoginFragment(), TeacherLoginFragment.TAG)
                .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        if (fragment is OnLoadButtonClickListener) {
            onLoadButtonClickListener = fragment
        } else {
            throw IllegalArgumentException("fragment $fragment must implement OnLoadButtonClickListener")
        }
    }
}
