package kozyriatskyi.anton.sked.flow.main

sealed class MainState(val route: String) {

    object Schedule : MainState(MainDestination.Schedule.route)

    object Settings : MainState(MainDestination.Settings.route)

    object About : MainState(MainDestination.About.route)

    object Audiences : MainState(MainDestination.Audiences.route)

}