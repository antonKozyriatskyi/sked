package kozyriatskyi.anton.sked.analytics

interface AnalyticsManager {

    fun logInfo(message: String)

    fun logUserType(type: UserType)

    fun logFailure(message: String? = null, throwable: Throwable? = null)

    enum class UserType {
        Student, Teacher
    }
}

