package kozyriatskyi.anton.sked.login.teacher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.login.LoginScreenScaffold
import kozyriatskyi.anton.sked.login.ui.LoginItemSelector
import kozyriatskyi.anton.sked.util.Callback
import kozyriatskyi.anton.sked.util.injectViewModel

/**
 * Created by Backbase R&D B.V. on 19.03.2022.
 */

@Composable
fun TeacherLoginScreen(
    viewModel: TeacherLoginViewModel = injectViewModel { Injector.loginComponent().teacherViewModel() },
    onScheduleLoaded: Callback,
    onExitClick: Callback,
) {
    val state by viewModel.state.collectAsState()

    val departments by derivedStateOf { state.departments.orEmpty() }
    val selectedDepartment by derivedStateOf { state.selectedDepartment }

    val teachers by derivedStateOf { state.teachers.orEmpty() }
    val selectedTeacher by derivedStateOf { state.selectedTeacher }

    val showLoading by derivedStateOf { state.showLoading }
    val enableUi by derivedStateOf { state.enableUi }
    val isLoaded by derivedStateOf { state.isLoaded }

    val error by derivedStateOf { state.error }

    val showConnectionError by viewModel.showConnectionError.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.openScheduleScreenEvent.collect {
            onScheduleLoaded()
        }
    }

    LoginScreenScaffold(
        showConnectionError = showConnectionError,
        enableButton = enableUi && isLoaded,
        onButtonClick = viewModel::loadSchedule
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            AnimatedVisibility(
                visible = showLoading,
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LoginItemSelector(
                title = stringResource(R.string.login_faculty),
                items = departments,
                selectedItem = selectedDepartment,
                enable = enableUi && selectedDepartment != null,
                onItemSelected = viewModel::chooseDepartment
            )

            Spacer(modifier = Modifier.height(8.dp))

            LoginItemSelector(
                title = stringResource(R.string.login_course),
                items = teachers,
                selectedItem = selectedTeacher,
                enable = enableUi && selectedTeacher != null,
                onItemSelected = viewModel::chooseTeacher
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (error != null) {
                val title = when (error!!.type) {
                    TeacherLoginErrorType.Departments -> stringResource(R.string.login_error_departments)
                    TeacherLoginErrorType.Teachers -> stringResource(R.string.login_error_teachers)
                    TeacherLoginErrorType.Schedule -> stringResource(R.string.login_error_schedule)
                }

                val message = error!!.message

                AlertDialog(
                    onDismissRequest = { },
                    title = { Text(text = title) },
                    text = { Text(text = message) },
                    confirmButton = {
                        TextButton(onClick = viewModel::retry) {
                            Text(text = stringResource(R.string.retry))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onExitClick) {
                            Text(text = stringResource(R.string.exit))
                        }
                    }
                )
            }
        }
    }
}
