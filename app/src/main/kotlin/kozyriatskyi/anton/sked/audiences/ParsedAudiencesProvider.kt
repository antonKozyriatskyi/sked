package kozyriatskyi.anton.sked.audiences

import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork
import kozyriatskyi.anton.sked.repository.AudiencesProvider
import kozyriatskyi.anton.sked.repository.StartEndTimePair
import kozyriatskyi.anton.sked.repository.Time
import kozyriatskyi.anton.sutparser.AudiencesParser
import kozyriatskyi.anton.sutparser.ParsedItem

class ParsedAudiencesProvider(private val parser: AudiencesParser) : AudiencesProvider {

    override fun getAudiences(date: String, lessonStart: String, lessonEnd: String): List<AudienceNetwork> {
        return parser.getAudiences(date, lessonStart, lessonEnd)
                .map {
                    AudienceNetwork(
                            number = it.number,
                            isFree = it.isFree,
                            note = it.note,
                            capacity = it.capacity)
                }
    }

    override fun getTimes(): StartEndTimePair {
        val (start, end) = parser.getTimes()

        fun mapToTimes(parsedItems: List<ParsedItem>): List<Time> = parsedItems.map { Time(id = it.id, value = it.value) }

        return StartEndTimePair(mapToTimes(start), mapToTimes(end))
    }
}