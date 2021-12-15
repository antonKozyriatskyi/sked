package kozyriatskyi.anton.sked.intro

import android.os.Bundle
import android.view.View
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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.composethemeadapter.MdcTheme
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.login.LoginView
import kozyriatskyi.anton.sked.navigation.Destination
import kozyriatskyi.anton.sked.navigation.Navigator
import kozyriatskyi.anton.sked.util.takeIf
import javax.inject.Inject

class IntroFragment : Fragment(R.layout.fragment_intro) {

    companion object {
        private const val ARG_ALLOW_BACK_NAVIGATION = "arg_can_go_back"

        fun createArgs(allowBackNavigation: Boolean): Bundle {
            return bundleOf(ARG_ALLOW_BACK_NAVIGATION to allowBackNavigation)
        }
    }

    @Inject
    lateinit var navigator: Navigator

    private val allowBackNavigation: Boolean by lazy {
        arguments?.getBoolean(ARG_ALLOW_BACK_NAVIGATION) ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.inject(this)

        view.findViewById<ComposeView>(R.id.intro_compose).apply {
            setContent {
                MdcTheme {
                    val icon = if (allowBackNavigation) Icons.Default.ArrowBack else null
                    val onNavigateUp = if (allowBackNavigation) {
                        { navigator.pop() }
                    } else null

                    IntroScreen(
                        navigationIcon = icon,
                        onNavigateUp = onNavigateUp,
                        onStudentClick = {
                            navigator.goTo(Destination.Login(LoginView.UserType.STUDENT))
                        },
                        onTeacherClick = {
                            navigator.goTo(Destination.Login(LoginView.UserType.TEACHER))
                        }
                    )
                }
            }
        }
    }
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