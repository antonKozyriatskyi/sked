package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork

typealias StartEndTimePair = Pair<List<Time>, List<Time>>

interface AudiencesProvider {
    suspend fun getAudiences(date: String, lessonStart: String, lessonEnd: String): List<AudienceNetwork>
    suspend fun getTimes(): StartEndTimePair
}