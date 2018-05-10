package kozyriatskyi.anton.sked.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Button
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.login.student.StudentLoginFragment
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginFragment

class LoginActivity : MvpAppCompatActivity(), LoginView, OnInternetConnectionChangeListener,
        OnLoadingStateChangeListener {

    companion object {
        const val EXTRA_MODE = "mode"

        fun start(context: Context, userType: UserType) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(EXTRA_MODE, userType)
            context.startActivity(intent)
        }
    }

    interface OnLoadButtonClickListener {
        fun onLoadButtonClick()
    }

    enum class UserType { STUDENT, TEACHER }

    private lateinit var onLoadButtonClickListener: OnLoadButtonClickListener

    private lateinit var loadButton: Button

    private lateinit var toolbarTitle: String

    private lateinit var snackBar: Snackbar

    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun providePresenter(): LoginPresenter {
        val userType = intent.getSerializableExtra(EXTRA_MODE) ?: throw NullPointerException("userType must not be null")
        //TODO use dagger to provide dependency
        return LoginPresenter(userType as UserType, ResourceManager(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_DarkActionBar)
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
