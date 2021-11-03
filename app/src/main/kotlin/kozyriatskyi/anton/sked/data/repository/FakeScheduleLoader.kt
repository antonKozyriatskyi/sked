package kozyriatskyi.anton.sked.data.repository

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.util.DateManipulator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by Anton on 10.08.2017.
 */
class FakeScheduleLoader : ScheduleProvider {

    private val datePattern = "dd.MM.yyyy"
    private val dateFormatter = DateTimeFormatter.ofPattern(datePattern)

    private val teachers = arrayListOf("Яскевич", "Марченко", "Онищенко", "Жебка", "Хтось")
    private val names = arrayListOf("ООП-C#", "Конструювання програмного забезпечення", "English",
            "Java Script", "Бази даних", "Java", "Архітектура")
    private val shortNames = arrayListOf("C#", "КПЗ", "Eng", "JS", "БД", "Java", "Арх-ра")
    private val cabinets = Array(999) { "$it" }
    private val numbers = Array(10) { "$it" }
    private val types = arrayOf("Лк", "Пз", "Лб", "Екз", "Зал", "Сем")
    private val dateManipulator = DateManipulator()
    private val dates = Array(4 * 5 * 5) {
        val a = dateManipulator.getWeekRange().first().plusDays(it.toLong())
        a.format(dateFormatter)
    }


    override fun getSchedule(
        user: User,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<LessonNetwork> {
        Thread.sleep(1000)

        val random = Random()
        val lessons = ArrayList<LessonNetwork>()
        var di = -1
        for (i in 0..5 * 4 * 5) {
            if (i % 4 == 0) di++
            lessons.add(
                LessonNetwork(
                    date = LocalDate.parse(dates[di], dateFormatter),
                    number = numbers[random.nextInt(numbers.size)],
                    type = types[random.nextInt(types.size)],
                    cabinet = cabinets[random.nextInt(cabinets.size)],
                    shortName = shortNames[random.nextInt(shortNames.size)],
                    name = names[random.nextInt(names.size)],
                    addedOnDate = "01.01.2017",
                    addedOnTime = "15:50",
                    who = teachers[random.nextInt(teachers.size)],
                    whoShort = teachers[random.nextInt(teachers.size)]
                )
            )
        }

        return lessons
    }
}