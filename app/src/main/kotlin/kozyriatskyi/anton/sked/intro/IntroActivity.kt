package kozyriatskyi.anton.sked.intro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.login.LoginActivity

@Suppress("UNUSED_PARAMETER")
class IntroActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }

    fun onStudentClick(view: View) = LoginActivity.start(this, LoginActivity.UserType.STUDENT)

    fun onTeacherClick(view: View) = LoginActivity.start(this, LoginActivity.UserType.TEACHER)
}
