package kozyriatskyi.anton.sked.intro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.login.LoginActivity
import kozyriatskyi.anton.sked.login.LoginView

@Suppress("UNUSED_PARAMETER")
class IntroActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_ActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }

    fun onStudentClick(view: View) = LoginActivity.start(this, LoginView.UserType.STUDENT)

    fun onTeacherClick(view: View) = LoginActivity.start(this, LoginView.UserType.TEACHER)
}
