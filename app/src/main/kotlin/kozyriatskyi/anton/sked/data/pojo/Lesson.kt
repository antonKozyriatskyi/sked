package kozyriatskyi.anton.sked.data.pojo

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.util.DateFormatter
import java.time.LocalDate


/**
 * Created by Anton on 23.02.2017.
 */

data class LessonNetwork(
    val date: LocalDate,
    val number: String,
    val type: String,
    val cabinet: String,
    val shortName: String,
    val name: String,
    val addedOnDate: String,
    val addedOnTime: String,
    val who: String,
    val whoShort: String
)

@Parcelize
data class LessonUi(
    val date: LocalDate,
    val shortDate: String,
    val number: String,
    val type: String,
    val cabinet: String,
    @ColorRes val typeColorRes: Int,
    val shortName: String,
    val name: String,
    val addedOnDate: String,
    val addedOnTime: String,
    val who: String,
    val whoShort: String,
    val time: String
) : Parcelable

@Entity(tableName = "lessons")
data class LessonDb(
    var date: LocalDate,
    var number: String,
    var type: String,
    var cabinet: String,
    @ColumnInfo(name = "short_name") var shortName: String,
    var name: String,
    @ColumnInfo(name = "added_on_date") var addedOnDate: String,
    @ColumnInfo(name = "added_on_time") var addedOnTime: String,
    var who: String,
    @ColumnInfo(name = "who_short") var whoShort: String
) {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

class LessonMapper(
    private val dateFormatter: DateFormatter
) {

    fun networkToDb(items: List<LessonNetwork>): List<LessonDb> = items.map {
        LessonDb(
            it.date,
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

    fun dbToView(items: List<LessonDb>): List<LessonUi> = items.map {
        LessonUi(
            date = it.date,
            shortDate = dateFormatter.short(it.date),
            number = it.number,
            type = it.type,
            cabinet = it.cabinet,
            typeColorRes = color(it.type),
            shortName = it.shortName,
            name = it.name,
            addedOnDate = it.addedOnDate,
            addedOnTime = it.addedOnTime,
            who = it.who,
            whoShort = it.whoShort,
            time = time(it.number)
        )
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

    @ColorRes
    private fun color(type: String): Int = when (type) {
        "Лк" -> R.color.lessonTypeLecture
        "Пз" -> R.color.lessonTypePractice
        "Лб" -> R.color.lessonTypeLab
        "Екз", "Экз" -> R.color.lessonTypeExamLight
        "Зал", "Зач" -> R.color.lessonTypeTest
        "Сем" -> R.color.lessonTypeSeminar
        else -> R.color.lessonTypeUnknown
    }
}