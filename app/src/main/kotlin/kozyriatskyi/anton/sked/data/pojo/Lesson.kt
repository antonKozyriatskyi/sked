package kozyriatskyi.anton.sked.data.pojo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.ColorInt
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.repository.ResourceManager


/**
 * Created by Anton on 23.02.2017.
 */

data class LessonNetwork(val date: String,
                         val number: String,
                         val type: String,
                         val cabinet: String,
                         val shortName: String,
                         val name: String,
                         val addedOnDate: String,
                         val addedOnTime: String,
                         val who: String,
                         val whoShort: String)

data class LessonUi(val date: String,
                    val number: String,
                    val type: String,
                    val cabinet: String,
                    @ColorInt val typeColor: Int,
                    val shortName: String,
                    val name: String,
                    val addedOnDate: String,
                    val addedOnTime: String,
                    val who: String,
                    val whoShort: String,
                    val time: String)

@Entity(tableName = "lessons")
data class LessonDb(var date: String,
                    var number: String,
                    var type: String,
                    var cabinet: String,
                    @ColumnInfo(name = "short_name") var shortName: String,
                    var name: String,
                    @ColumnInfo(name = "added_on_date") var addedOnDate: String,
                    @ColumnInfo(name = "added_on_time") var addedOnTime: String,
                    var who: String,
                    @ColumnInfo(name = "who_short") var whoShort: String) {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

class LessonMapper(private val resourceManager: ResourceManager) {

    fun networkToDb(items: List<LessonNetwork>): List<LessonDb> {
        return items.map {
            LessonDb(it.date,
                    it.number,
                    it.type,
                    it.cabinet,
                    it.shortName,
                    it.name,
                    it.addedOnDate,
                    it.addedOnTime,
                    it.who,
                    it.whoShort
            )
        }
    }

    fun dbToView(items: List<LessonDb>): List<LessonUi> {
        return items.map {
            LessonUi(it.date,
                    it.number,
                    it.type,
                    it.cabinet,
                    color(it.type),
                    it.shortName,
                    it.name,
                    it.addedOnDate,
                    it.addedOnTime,
                    it.who,
                    it.whoShort,
                    time(it.number))
        }
    }

    private fun time(number: String) = when (number) {
        "1" -> "08:00 - 09:35"
        "2" -> "09:45 - 11:20"
        "3" -> "11:45 - 13:20"
        "4" -> "13:30 - 15:05"
        "5" -> "15:15 - 16:50"
        "6" -> "17:00 - 18:35"
        "7" -> "18:45 - 20:20"
        "8" -> "20:30 - 22:05"
        else -> "N/A"
    }

    private fun color(type: String): Int = when (type) {
        "Лк" -> resourceManager.getColor(R.color.lessonTypeLecture)
        "Пз" -> resourceManager.getColor(R.color.lessonTypePractice)
        "Лб" -> resourceManager.getColor(R.color.lessonTypeLab)
        "Екз", "Экз" -> resourceManager.getColor(R.color.lessonTypeExamLight)
        "Зал", "Зач" -> resourceManager.getColor(R.color.lessonTypeTest)
        "Сем" -> resourceManager.getColor(R.color.lessonTypeSeminar)
        else -> resourceManager.getColor(R.color.lessonTypeUnknown)
    }
}