package kozyriatskyi.anton.sked.data.repository

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.repository.ScheduleLoader
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Anton on 10.08.2017.
 */
class FakeScheduleLoader : ScheduleLoader {

    private val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }
    private val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private val teachers = arrayListOf("Яскевич", "Марченко", "Онищенко", "Жебка", "Хтось")
    private val names = arrayListOf("ООП-C#", "Конструювання програмного забезпечення", "English",
            "Java Script", "Бази даних", "Java", "Архітектура")
    private val shortNames = arrayListOf("C#", "КПЗ", "Eng", "JS", "БД", "Java", "Арх-ра")
    private val cabinets = Array(999) { "$it" }
    private val numbers = Array(10) { "$it" }
    private val types = arrayOf("Лк", "Пз", "Лб", "Екз", "Зал", "Сем")
    private val dates = Array(4 * 5 * 5) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        formatter.format(calendar.time)
    }


    override fun getSchedule(user: User): List<LessonNetwork> {
        Thread.sleep(5000)

        val random = Random()
        val lessons = ArrayList<LessonNetwork>()
        var di = -1
        for (i in 0..5 * 4 * 5) {
            if (i % 4 == 0) di++
            lessons.add(LessonNetwork(date = dates[di],
                    number = numbers[random.nextInt(numbers.size)],
                    type = types[random.nextInt(types.size)],
                    cabinet = cabinets[random.nextInt(cabinets.size)],
                    shortName = shortNames[random.nextInt(shortNames.size)],
                    name = names[random.nextInt(names.size)],
                    addedOnDate = "01.01.2017",
                    addedOnTime = "15:50",
                    who = teachers[random.nextInt(teachers.size)],
                    whoShort = teachers[random.nextInt(teachers.size)]))
        }

        return lessons
    }
}