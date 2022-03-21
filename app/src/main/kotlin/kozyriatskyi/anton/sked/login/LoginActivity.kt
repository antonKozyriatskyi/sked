package kozyriatskyi.anton.sked.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import com.google.android.material.composethemeadapter.MdcTheme
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.login.ui.LoginScreen
import kozyriatskyi.anton.sked.main.MainActivity

class LoginActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_TYPE = "type"

        fun start(context: Context, userType: LoginUserType) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(EXTRA_USER_TYPE, userType)
            context.startActivity(intent)
        }
    }

    private val userType: LoginUserType by lazy {
        intent.getSerializableExtra(EXTRA_USER_TYPE) as? LoginUserType
            ?: throw NullPointerException("userType must not be null")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            Injector.createLoginComponent(userType)
        }

        val view = ComposeView(this).apply {
            setContent {
                MdcTheme {
                    LoginScreen(
                        userType = userType,
                        onNavigateUp = { finish() },
                        onScheduleLoaded = {
                            MainActivity.start(this@LoginActivity)
                            finish()
                        },
                        onExitClick = { finish() }
                    )
                }
            }
        }

        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            Injector.clear(this)
        }
    }
}
