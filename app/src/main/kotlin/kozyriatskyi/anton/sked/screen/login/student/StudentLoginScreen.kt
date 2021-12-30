package kozyriatskyi.anton.sked.screen.login.student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector2
import kozyriatskyi.anton.sked.util.UnitCallback
import kozyriatskyi.anton.sked.util.rememberMutableState

@Composable
fun StudentLoginScreen(
    navigationIcon: ImageVector,
    onNavigateUp: UnitCallback,
    onScheduleLoaded: UnitCallback,
    viewModel: StudentLoginViewModel = Injector2.studentLoginComponent().viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.login_student))
                },
                navigationIcon = {
                    IconButton(onNavigateUp) {
                        Icon(
                            navigationIcon,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) {
        Column {
            val state by viewModel.state.collectAsState()

            val faculties by derivedStateOf { state.faculties.orEmpty() }
            var facultiesExpanded by rememberMutableState(value = false)

            AnimatedVisibility(visible = state.showProgress) {
                LinearProgressIndicator()
            }

            DropdownMenu(
                expanded = facultiesExpanded,
                onDismissRequest = { facultiesExpanded = false },
            ) {
                faculties.forEach {
                    DropdownMenuItem(onClick = { viewModel.selectFaculty(it) }) {
                        Text(text = it.value)
                    }
                }
            }

            val courses by derivedStateOf { state.courses.orEmpty() }
            var coursesExpanded by rememberMutableState(value = false)

            DropdownMenu(
                expanded = coursesExpanded,
                onDismissRequest = { coursesExpanded = false }
            ) {
                courses.forEach {
                    DropdownMenuItem(onClick = { viewModel.selectCourse(it) }) {
                        Text(text = it.value)
                    }
                }
            }

            val groups by derivedStateOf { state.groups.orEmpty() }
            var groupsExpanded by rememberMutableState(value = false)

            DropdownMenu(
                expanded = groupsExpanded,
                onDismissRequest = { groupsExpanded = false }
            ) {
                groups.forEach {
                    DropdownMenuItem(onClick = { viewModel.selectGroup(it) }) {
                        Text(text = it.value)
                    }
                }
            }

            TextButton(onClick = { viewModel.loadSchedule() }) {
                Text(text = stringResource(R.string.login_load_schedule))
            }

            if (state.scheduleLoaded) {
                onScheduleLoaded()
            }
        }
    }
}
