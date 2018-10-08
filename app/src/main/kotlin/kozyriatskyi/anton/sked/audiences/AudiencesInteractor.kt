package kozyriatskyi.anton.sked.audiences

import io.reactivex.Single
import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork
import kozyriatskyi.anton.sked.repository.AudiencesProvider
import kozyriatskyi.anton.sked.repository.Time
import kozyriatskyi.anton.sked.util.safeSingle

class AudiencesInteractor(private val audiencesProvider: AudiencesProvider) {

    fun getAudiences(date: String, lessonStart: String, lessonEnd: String): Single<List<AudienceNetwork>> =
            safeSingle { audiencesProvider.getAudiences(date, lessonStart, lessonEnd) }

    fun getTimes(): Single<Pair<List<Time>, List<Time>>> = safeSingle(audiencesProvider::getTimes)
}