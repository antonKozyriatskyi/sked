package kozyriatskyi.anton.sked.login.teacher

data class TeacherLoginError(
    val type: TeacherLoginErrorType,
    val message: String,
)