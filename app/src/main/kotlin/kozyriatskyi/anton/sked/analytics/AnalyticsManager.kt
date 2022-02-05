package kozyriatskyi.anton.sked.analytics

/**
 * Created by Backbase R&D B.V. on 17.10.2021.
 */
interface AnalyticsManager {

    fun logInfo(message: String)

    fun logUserType(type: UserType)

    fun logFailure(message: String? = null, throwable: Throwable? = null)

    enum class UserType {
        Student, Teacher
    }
}

