package kozyriatskyi.anton.sked.flow.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kozyriatskyi.anton.sked.intro.IntroScreen
import kozyriatskyi.anton.sked.screen.login.student.StudentLoginScreen
import kozyriatskyi.anton.sked.screen.login.teacher.TeacherLoginScreen
import kozyriatskyi.anton.sked.util.UnitCallback

@Composable
fun LoginFlow(
    route: String,
    allowBackNavigationFromUserSelection: Boolean,
    onScheduleLoaded: UnitCallback,
    onNavigateUp: UnitCallback?
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginFlowDestination.UserSelection.route,
        route = route,
    ) {
        composable(LoginFlowDestination.UserSelection.route) {
            val icon = Icons.Default.ArrowBack.takeIf { allowBackNavigationFromUserSelection }
            val onIntroNavigateUp = onNavigateUp.takeIf { allowBackNavigationFromUserSelection }

            IntroScreen(
                navigationIcon = icon,
                onNavigateUp = onIntroNavigateUp,
                onStudentClick = {
                    navController.navigate(LoginFlowDestination.Student.route)
                },
                onTeacherClick = {
                    navController.navigate(LoginFlowDestination.Teacher.route)
                }
            )
        }

        composable(LoginFlowDestination.Student.route) {
            StudentLoginScreen(
                navigationIcon = Icons.Default.ArrowBack,
                onNavigateUp = navController::navigateUp,
                onScheduleLoaded = onScheduleLoaded
            )
        }

        composable(LoginFlowDestination.Teacher.route) {
            TeacherLoginScreen(
                navigationIcon = Icons.Default.ArrowBack,
                onNavigateUp = navController::navigateUp,
                onScheduleLoaded = onScheduleLoaded
            )
        }
    }
}

