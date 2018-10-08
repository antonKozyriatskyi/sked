package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork

typealias StartEndTimePair = Pair<List<Time>, List<Time>>

interface AudiencesProvider {
    fun getAudiences(date: String, lessonStart: String, lessonEnd: String): List<AudienceNetwork>
    fun getTimes(): StartEndTimePair
}