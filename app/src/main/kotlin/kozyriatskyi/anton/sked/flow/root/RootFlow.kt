package kozyriatskyi.anton.sked.flow.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Injector2
import kozyriatskyi.anton.sked.flow.login.LoginFlow
import kozyriatskyi.anton.sked.flow.main.MainFlow
import kozyriatskyi.anton.sked.screen.splash.SplashScreen
import javax.inject.Inject

@Composable
fun RootFlow(
    viewModel: RootFlowViewModel = Injector2.rootComponent().viewModel()
) {
    LaunchedEffect(viewModel) {
        viewModel.check()
    }

    val state by viewModel.state.collectAsState()

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = state.route) {
        composable(RootDestination.Splash.route) {
            SplashScreen()
        }

        composable(
            route = "${RootDestination.Login.route}/{isRootScreen}",
            arguments = listOf(navArgument("isRootScreen") { type = NavType.BoolType })
        ) {
            val isRootScreen = it.arguments!!.getBoolean("isRootScreen")
            LoginFlow(
                route = it.destination.route!!,
                allowBackNavigationFromUserSelection = isRootScreen.not(),
                onNavigateUp = navController::navigateUp,
                onScheduleLoaded = { navController.navigate(RootDestination.Main.route) }
            )
        }

        composable(RootDestination.Main.route) {
            MainFlow(
                route = it.destination.route!!,
                onReloginClick = viewModel::relogin
            )
        }
    }

    when (state) {
        is RootState.Login -> {
            navController.navigate("${state.route}/${(state as RootState.Login).isRootScreen}")
        }
        else -> navController.navigate(state.route)
    }
}

enum class RootDestination {
    Splash,
    Login,
    Main;

    val route: String get() = this.toString()
}


class RootFlowViewModel @Inject constructor(private val userInfoStorage: UserInfoStorage) : ViewModel() {

    private val _state = MutableStateFlow<RootState>(RootState.Splash)
    val state: StateFlow<RootState> get() = _state

    fun check() {
        _state.value = if (userInfoStorage.hasUser()) {
            RootState.Login(true)
        } else {
            RootState.Main
        }
    }

    fun relogin() {
        _state.value = RootState.Login(false)
    }
}

sealed class RootState(val route: String) {

    object Splash : RootState(RootDestination.Splash.route)

    class Login(val isRootScreen: Boolean) : RootState(RootDestination.Login.route)

    object Main : RootState(RootDestination.Main.route)
}