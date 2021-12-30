package kozyriatskyi.anton.sked.flow.main

enum class MainDestination {
    Schedule,
    Settings,
    About,
    Audiences;

    val route: String get() = this.toString()
}