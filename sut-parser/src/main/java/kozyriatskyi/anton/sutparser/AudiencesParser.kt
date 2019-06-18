package kozyriatskyi.anton.sutparser

import org.jsoup.select.Elements
import java.util.*
import java.util.regex.Pattern

class AudiencesParser {

    companion object {
        private const val BASE_URL = "http://e-rozklad.dut.edu.ua/timeTable/freeClassroom"
    }

//    <li data-content="Количество мест: 45<br>Примечание: ТТ" data-rel="popover" class="" data-original-title="" title="">
//        <a target="_blank" href="/timeTable/classroom?TimeTableForm%5Bclassroom%5D=47">507</a>
//    </li>

    /**
     * Downloads a list of audiences that contains all audiences for the [date] including
     * that are free from [lessonStart] till [lessonEnd].
     *
     * @param lessonStart number of lesson to look for free audiences
     * @param lessonEnd number of lesson to look for free audiences
     * @param date date to look for audiences. Should be in dd.MM.yyyy format
     * @returns list of audiences for specified [date], [lessonStart] and [lessonEnd]
     * */
    fun getAudiences(date: String, lessonStart: String, lessonEnd: String): List<ParsedAudience> {
        val url = "$BASE_URL?TimeTableForm[date1]=$date&TimeTableForm[lessonStart]=$lessonStart&TimeTableForm[lessonEnd]=$lessonEnd"
        val document = loadDocument(url)
        val audiencesCells = document.body()
                .getElementsByClass("classrooms-list")
                .first()
                .getElementsByTag("li")

        return extractAudiencesInfo(audiencesCells)
    }

    private fun extractAudiencesInfo(audiencesCells: Elements): List<ParsedAudience> {
        val audiences = ArrayList<ParsedAudience>(audiencesCells.size)

        val capacityAndNotePattern = Pattern.compile("Количество мест: (.*)<br>Примечание: (.*)")

        for (i in audiencesCells.indices) {
            val cell = audiencesCells[i]

            val capacityAndNote = cell.attr("data-content")
            var capacity: String
            var note: String

            val matcher = capacityAndNotePattern.matcher(capacityAndNote)
            if (matcher.find()) {
                capacity = matcher.group(1)
                note = matcher.group(2)
            } else {
                throw ParseException("couldn't parse audiences")
            }

            val isFree = cell.className() != "btn-danger"
            val number = cell.getElementsByTag("a").first().text()

            audiences.add(ParsedAudience(number, isFree, note, capacity))
        }

        return audiences
    }

    /**
     * Downloads available times to choose when selecting a free audiences
     *
     * @returns pair of start as [Pair.first] and end times as [Pair.second],
     * to be chosen when selecting audience
     * */
    fun getTimes(): Pair<List<ParsedItem>, List<ParsedItem>> {
        val document = loadDocument(BASE_URL)
        val body = document.body()

        fun extractTimes(elementId: String): List<ParsedItem> = body.getElementById(elementId)
                .getElementsByTag("option")
                .drop(1)
                .map(::ParsedItem)

        val startTimes = extractTimes("TimeTableForm_lessonStart")
        val endTimes = extractTimes("TimeTableForm_lessonEnd")

        return Pair(startTimes, endTimes)
    }
}