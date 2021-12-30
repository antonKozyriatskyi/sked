package kozyriatskyi.anton.sked.flow.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kozyriatskyi.anton.sked.di.Injector2
import kozyriatskyi.anton.sked.screen.about.AboutScreen
import kozyriatskyi.anton.sked.screen.audiences.AudiencesScreen
import kozyriatskyi.anton.sked.screen.schedule.ScheduleScreen
import kozyriatskyi.anton.sked.screen.settings.SettingsScreen
import kozyriatskyi.anton.sked.util.UnitCallback

@Composable
fun MainFlow(
    route: String,
    onReloginClick: UnitCallback,
    viewModel: MainFlowViewModel = Injector2.mainComponent().viewModel()
) {
    val state by viewModel.state.collectAsState()

    val navController = rememberNavController()

    NavHost(
        route = route,
        navController = navController,
        startDestination = state.route,
    ) {
        composable(MainDestination.Schedule.route) {
            ScheduleScreen(
                onShowAudiencesClick = viewModel::goToAudiences,
                onSettingsClick = viewModel::goToSettings,
                onAboutClick = viewModel::goToAbout,
                onReloginClick = onReloginClick
            )
        }

        composable(MainDestination.Audiences.route) {
            AudiencesScreen()
        }

        composable(MainDestination.Settings.route) {
            SettingsScreen()
        }

        composable(MainDestination.About.route) {
            AboutScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}