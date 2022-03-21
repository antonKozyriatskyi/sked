package kozyriatskyi.anton.sked.login.student

data class StudentLoginError(
    val type: StudentLoginErrorType,
    val message: String,
)