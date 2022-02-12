package kozyriatskyi.anton.sked.intro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.login.LoginActivity
import kozyriatskyi.anton.sked.login.LoginView
import kozyriatskyi.anton.sked.util.takeIf

@Suppress("UNUSED_PARAMETER")
class IntroActivity : AppCompatActivity() {

    companion object {
        private const val ARG_ALLOW_BACK_NAVIGATION = "arg_can_go_back"

        fun start(context: Context, allowBackNavigation: Boolean) {
            val intent = Intent(context, IntroActivity::class.java).apply {
                putExtra(ARG_ALLOW_BACK_NAVIGATION, allowBackNavigation)
            }

            context.startActivity(intent)
        }
    }

    private val allowBackNavigation: Boolean by lazy {
        intent.getBooleanExtra(ARG_ALLOW_BACK_NAVIGATION, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        val view = ComposeView(this).apply {
            setContent {
                MdcTheme {
                    val icon = if (allowBackNavigation) Icons.Default.ArrowBack else null
                    val onNavigateUp = if (allowBackNavigation) {
                        { finish() }
                    } else null

                    IntroScreen(
                        navigationIcon = icon,
                        onNavigateUp = onNavigateUp,
                        onStudentClick = ::onStudentClick,
                        onTeacherClick = ::onTeacherClick
                    )
                }
            }
        }

        setContentView(view)
    }

    private fun onStudentClick() = LoginActivity.start(this, LoginView.UserType.STUDENT)

    private fun onTeacherClick() = LoginActivity.start(this, LoginView.UserType.TEACHER)
}

@Composable
fun IntroScreen(
    navigationIcon: ImageVector?,
    onNavigateUp: (() -> Unit)?,
    onStudentClick: () -> Unit,
    onTeacherClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = {
                    Text(text = stringResource(id = R.string.intro_title))
                },
                navigationIcon = takeIf(navigationIcon != null && onNavigateUp != null) {
                    IconButton(onClick = onNavigateUp!!) {
                        Icon(
                            imageVector = navigationIcon!!,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            SecondaryButton(onClick = onStudentClick) {
                Text(text = stringResource(id = R.string.login_student))
            }

            SecondaryButton(onClick = onTeacherClick) {
                Text(text = stringResource(id = R.string.login_teacher))
            }
        }
    }
}


@Composable
private fun SecondaryButton(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val colors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.onSecondary
    )

    Button(
        onClick = onClick,
        colors = colors,
        content = content
    )
}

@Preview
@Composable
fun IntroScreenPreview_allowBack() {
    IntroScreen(
        navigationIcon = Icons.Default.ArrowBack,
        onNavigateUp = { },
        onStudentClick = { },
        onTeacherClick = { }
    )
}

@Preview
@Composable
fun IntroScreenPreview_notAllowBack() {
    IntroScreen(
        navigationIcon = null,
        onNavigateUp = null,
        onStudentClick = { },
        onTeacherClick = { }
    )
}
