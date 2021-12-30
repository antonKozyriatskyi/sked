package kozyriatskyi.anton.sked.flow.login

enum class LoginFlowDestination {
    UserSelection, Student, Teacher;

    val route: String get() = this.toString()
}