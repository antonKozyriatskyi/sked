package kozyriatskyi.anton.sked.navigation

import androidx.navigation.NavController
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.intro.IntroFragment
import kozyriatskyi.anton.sked.login.LoginFragment

/**
 * Created by Backbase R&D B.V. on 27.11.2021.
 */
class Navigator {

    private var pendingActions: MutableList<(NavController) -> Unit> = mutableListOf()

    private var navController: NavController? = null
        set(value) {
            if (field == null && value != null) {
                pendingActions.forEach { it(value) }
            } else if (field != null && value != null) {
                val saveState = field!!.saveState()
                value.restoreState(saveState)
            }

            field = value
        }

    fun setStartDestination(destination: Destination) {
        execute { navController ->
            val navGraph = navController.navInflater.inflate(R.navigation.app_graph)

            val startDestinationId = when (destination) {
                is Destination.Intro -> R.id.dest_intro_screen
                is Destination.Schedule -> R.id.dest_schedule_screen
                else -> throw IllegalArgumentException("unsupported start destination: $destination")
            }

            navGraph.setStartDestination(startDestinationId)

            navController.graph = navGraph
        }
    }

    fun goTo(destination: Destination) {
        execute { navController ->
            when (destination) {
                is Destination.Intro -> {
                    val args = IntroFragment.createArgs(destination.allowBackNavigation)

                    when (navController.currentDestination?.id) {
                        R.id.dest_schedule_screen -> {
                            navController.navigate(R.id.action_schedule_to_intro_screen, args)
                        }
                    }
                }
                Destination.About -> navController.navigate(R.id.action_schedule_to_about_screen)
                Destination.Audiences -> navController.navigate(R.id.action_schedule_to_audiences_screen)
                Destination.Settings -> navController.navigate(R.id.action_schedule_to_settings_screen)
                is Destination.Login -> {
                    val args = LoginFragment.createArgs(destination.userType)
                    navController.navigate(R.id.action_intro_to_login_screen, args)
                }
                Destination.Schedule -> navController.navigate(R.id.action_login_to_schedule_screen)
            }
        }
    }

    fun pop() {
        execute { navController -> navController.popBackStack() }
    }

    fun updateNavController(navController: NavController) {
        this.navController = navController
    }

    private fun execute(action: (NavController) -> Unit) {
        navController?.let(action) ?: kotlin.run {
            pendingActions.add { navController -> action(navController) }
        }
    }
}

