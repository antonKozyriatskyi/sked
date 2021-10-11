package kozyriatskyi.anton.sked.audiences

import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork
import kozyriatskyi.anton.sked.repository.AudiencesProvider
import kozyriatskyi.anton.sked.repository.Time

class AudiencesInteractor(private val audiencesProvider: AudiencesProvider) {

    fun getAudiences(date: String, lessonStart: String, lessonEnd: String): Result<List<AudienceNetwork>> {
        return kotlin.runCatching { audiencesProvider.getAudiences(date, lessonStart, lessonEnd) }
    }


    fun getTimes(): Result<Pair<List<Time>, List<Time>>> = kotlin.runCatching {
        audiencesProvider.getTimes()
    }
}