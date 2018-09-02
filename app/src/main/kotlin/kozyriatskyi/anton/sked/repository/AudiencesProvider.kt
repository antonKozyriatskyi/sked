package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork
import kozyriatskyi.anton.sked.data.pojo.Item

typealias StartEndTimePair = Pair<List<Item>, List<Item>>

interface AudiencesProvider {
    fun getAudiences(date: String, lessonStart: Int, lessonEnd: Int): List<AudienceNetwork>
    fun getTimes(): StartEndTimePair
}