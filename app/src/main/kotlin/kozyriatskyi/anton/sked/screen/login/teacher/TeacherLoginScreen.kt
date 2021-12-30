package kozyriatskyi.anton.sked.screen.login.teacher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector2
import kozyriatskyi.anton.sked.util.UnitCallback
import kozyriatskyi.anton.sked.util.rememberMutableState

@Composable
fun TeacherLoginScreen(
    navigationIcon: ImageVector,
    onNavigateUp: UnitCallback,
    onScheduleLoaded: UnitCallback,
    viewModel: TeacherLoginViewModel = Injector2.teacherLoginComponent().viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.login_teacher))
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) {
        Column {
            val state by viewModel.state.collectAsState()

            val departments by derivedStateOf { state.departments.orEmpty() }
            var departmentsExpanded by rememberMutableState(value = false)

            AnimatedVisibility(visible = state.showProgress) {
                LinearProgressIndicator()
            }

            DropdownMenu(
                expanded = departmentsExpanded,
                onDismissRequest = { departmentsExpanded = false }
            ) {
                departments.forEach {
                    DropdownMenuItem(onClick = { viewModel.selectDepartment(it) }) {
                        Text(text = it.value)
                    }
                }
            }

            val teachers by derivedStateOf { state.teachers.orEmpty() }
            var teachersExpanded by rememberMutableState(value = false)

            DropdownMenu(
                expanded = teachersExpanded,
                onDismissRequest = { teachersExpanded = false },
                modifier = Modifier.toggleable(
                    true,
                    state.enableUi,
                    onValueChange = { }
                )
            ) {
                teachers.forEach {
                    DropdownMenuItem(onClick = { viewModel.selectTeacher(it) }) {
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