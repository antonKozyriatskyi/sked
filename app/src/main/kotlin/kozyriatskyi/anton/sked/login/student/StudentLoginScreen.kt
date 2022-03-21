package kozyriatskyi.anton.sked.login.student

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
fun StudentLoginScreen(
    viewModel: StudentLoginViewModel = injectViewModel {
        Injector.loginComponent().studentViewModel()
    },
    onScheduleLoaded: Callback,
    onExitClick: Callback,
) {
    val state by viewModel.state.collectAsState()

    val faculties by derivedStateOf { state.faculties.orEmpty() }
    val selectedFaculty by derivedStateOf { state.selectedFaculty }

    val courses by derivedStateOf { state.courses.orEmpty() }
    val selectedCourse by derivedStateOf { state.selectedCourse }

    val groups by derivedStateOf { state.groups.orEmpty() }
    val selectedGroup by derivedStateOf { state.selectedGroup }

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
                items = faculties,
                selectedItem = selectedFaculty,
                enable = enableUi && selectedFaculty != null,
                onItemSelected = viewModel::chooseFaculty
            )

            Spacer(modifier = Modifier.height(8.dp))

            LoginItemSelector(
                title = stringResource(R.string.login_course),
                items = courses,
                selectedItem = selectedCourse,
                enable = enableUi && selectedCourse != null,
                onItemSelected = viewModel::chooseCourse
            )

            Spacer(modifier = Modifier.height(8.dp))

            LoginItemSelector(
                title = stringResource(R.string.login_group),
                items = groups,
                selectedItem = selectedGroup,
                enable = enableUi && selectedGroup != null,
                onItemSelected = viewModel::chooseGroup
            )

            if (error != null) {
                val title = when (error!!.type) {
                    StudentLoginErrorType.Faculties -> stringResource(R.string.login_error_faculties)
                    StudentLoginErrorType.Courses -> stringResource(R.string.login_error_courses)
                    StudentLoginErrorType.Groups -> stringResource(R.string.login_error_groups)
                    StudentLoginErrorType.Schedule -> stringResource(R.string.login_error_schedule)
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
