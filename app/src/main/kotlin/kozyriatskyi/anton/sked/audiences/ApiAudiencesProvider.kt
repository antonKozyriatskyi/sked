package kozyriatskyi.anton.sked.audiences

import kozyriatskyi.anton.sked.data.api.ClassroomsApi
import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork
import kozyriatskyi.anton.sked.data.pojo.TimeNetwork
import kozyriatskyi.anton.sked.repository.AudiencesProvider
import kozyriatskyi.anton.sked.repository.StartEndTimePair
import kozyriatskyi.anton.sked.repository.Time

class ApiAudiencesProvider(private val api: ClassroomsApi) : AudiencesProvider {

    override suspend fun getAudiences(date: String, lessonStart: String, lessonEnd: String): List<AudienceNetwork> {
        return api.getAudiences(date, lessonStart, lessonEnd)
                .map {
                    AudienceNetwork(
                            number = it.number,
                            isFree = it.isFree,
                            note = it.note,
                            capacity = it.capacity)
                }
    }

    override suspend fun getTimes(): StartEndTimePair {
        val (start, end) = api.getTimes()

        fun mapToTimes(items: List<TimeNetwork>): List<Time> = items.map { Time(id = it.id, value = it.value) }

        return StartEndTimePair(mapToTimes(start), mapToTimes(end))
    }
}