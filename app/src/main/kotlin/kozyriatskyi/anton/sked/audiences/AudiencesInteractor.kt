package kozyriatskyi.anton.sked.audiences

import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork
import kozyriatskyi.anton.sked.repository.AudiencesProvider
import kozyriatskyi.anton.sked.repository.Time

class AudiencesInteractor(
    private val audiencesProvider: AudiencesProvider,
    private val analyticsManager: AnalyticsManager
) {

    suspend fun getAudiences(
        date: String,
        lessonStart: String,
        lessonEnd: String
    ): Result<List<AudienceNetwork>> = kotlin.runCatching {
        audiencesProvider.getAudiences(date, lessonStart, lessonEnd)
    }.onFailure {
        analyticsManager.logFailure(
            message = "Error loading classrooms",
            throwable = it
        )
    }


    suspend fun getTimes(): Result<Pair<List<Time>, List<Time>>> = kotlin.runCatching {
        audiencesProvider.getTimes()
    }
        .onFailure {
            analyticsManager.logFailure(
                message = "Error loading classroom times",
                throwable = it
            )
        }
}