package kozyriatskyi.anton.sutparser

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.util.*
import java.util.regex.Pattern

typealias StartEndTimePair = Pair<List<ParsedItem>, List<ParsedItem>>

class AudiencesParser {

    companion object {
        private const val BASE_URL = "http://e-rozklad.dut.edu.ua/timeTable/freeClassroom"
    }

//    <li data-content="Количество мест: 45<br>Примечание: ТТ" data-rel="popover" class="" data-original-title="" title="">
//        <a target="_blank" href="/timeTable/classroom?TimeTableForm%5Bclassroom%5D=47">507</a>
//    </li>

    // date in dd.MM.yyyy format
    fun getAudiences(date: String, lessonStart: Int, lessonEnd: Int): List<ParsedAudience> {
        val url = "$BASE_URL?TimeTableForm[date1]=$date&TimeTableForm[lessonStart]=$lessonStart&TimeTableForm[lessonEnd]=$lessonEnd"
        val document = Jsoup.connect(url).timeout(TIMEOUT).get()
        val audiencesCells = document.body().getElementsByClass("classrooms-list")
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
            var capacity = ""
            var note = ""

            val matcher = capacityAndNotePattern.matcher(capacityAndNote)
            if (matcher.find()) {
                capacity = matcher.group(1)
                note = matcher.group(2)
            }

            val isFree = cell.className() != "btn-danger"
            val number = cell.getElementsByTag("a").first().text()

            audiences.add(ParsedAudience(number, isFree, note, capacity))
        }

        return audiences
    }

    /**
     * returns start and end times, to be chosen when selecting audience
     * */
    fun getTimes(): StartEndTimePair {
        val document = Jsoup.connect(BASE_URL).timeout(TIMEOUT).get()

        val body = document.body()

        val startTimeDropdown = body.getElementById("TimeTableForm_lessonStart")
        val endTimeDropdown = body.getElementById("TimeTableForm_lessonEnd")

        fun extractItems(e: Elements): List<ParsedItem> = e.drop(1).map(::ParsedItem)

        val startTimes = extractItems(startTimeDropdown.getElementsByTag("option"))
        val endTimes = extractItems(endTimeDropdown.getElementsByTag("option"))

        return Pair(startTimes, endTimes)
    }
}