package kozyriatskyi.anton.sked.login.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.login.LoginUserType
import kozyriatskyi.anton.sked.login.student.StudentLoginScreen
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginScreen
import kozyriatskyi.anton.sked.util.Callback

/**
 * Created by Backbase R&D B.V. on 20.03.2022.
 */

@Composable
fun LoginScreen(
    userType: LoginUserType,
    onNavigateUp: Callback,
    onScheduleLoaded: Callback,
    onExitClick: Callback,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = {
                    val title = when (userType) {
                        LoginUserType.Student -> stringResource(R.string.login_student)
                        LoginUserType.Teacher -> stringResource(R.string.login_teacher)
                    }

                    Text(text = title)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            val width = when {
                maxWidth > 800.dp -> 800.dp
                maxWidth > 600.dp -> 600.dp
                maxWidth > 400.dp -> 400.dp
                else -> maxWidth
            }

            Box(
                modifier = Modifier
                    .width(width)
            ) {
                when (userType) {
                    LoginUserType.Student -> StudentLoginScreen(
                        onScheduleLoaded = onScheduleLoaded,
                        onExitClick = onExitClick
                    )
                    LoginUserType.Teacher -> TeacherLoginScreen(
                        onScheduleLoaded = onScheduleLoaded,
                        onExitClick = onExitClick
                    )
                }
            }
        }
    }
}