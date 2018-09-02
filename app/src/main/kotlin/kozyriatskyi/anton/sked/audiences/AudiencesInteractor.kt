package kozyriatskyi.anton.sked.audiences

import io.reactivex.Single
import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.repository.AudiencesProvider
import kozyriatskyi.anton.sked.util.safeSingle

class AudiencesInteractor(private val audiencesProvider: AudiencesProvider) {

    fun getAudiences(date: String, lessonStart: Int, lessonEnd: Int): Single<List<AudienceNetwork>> {
        return safeSingle { audiencesProvider.getAudiences(date, lessonStart, lessonEnd) }
    }

    fun getTimes(): Single<Pair<List<Item>, List<Item>>> = safeSingle(audiencesProvider::getTimes)
}