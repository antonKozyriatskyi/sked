package kozyriatskyi.anton.sked.navigation

import kozyriatskyi.anton.sked.login.LoginView

sealed class Destination {

    data class Intro(val allowBackNavigation: Boolean) : Destination()

    data class Login(val userType: LoginView.UserType) : Destination()

    object Schedule : Destination()

    object Audiences : Destination()

    object Settings : Destination()

    object About : Destination()
}