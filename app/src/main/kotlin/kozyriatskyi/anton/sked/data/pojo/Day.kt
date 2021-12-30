package kozyriatskyi.anton.sked.data.pojo

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.util.DateFormatter
import java.time.DayOfWeek
import java.time.LocalDate

@Parcelize
class DayUi(
    val date: LocalDate,
    @StringRes val name: Int,
    val shortDate: String,
    val lessons: List<LessonUi>
) : Parcelable

class DayMapper(
    private val lessonMapper: LessonMapper,
    private val dateFormatter: DateFormatter
) {

    fun createUiModel(date: LocalDate, lessons: List<LessonDb>): DayUi = DayUi(
        date = date,
        name = getName(date.dayOfWeek),
        shortDate = dateFormatter.short(date),
        lessons = lessonMapper.dbToView(lessons)
    )

    private fun getName(dayOfWeek: DayOfWeek): Int {
        val daysOfWeek = arrayOf(
            R.string.day_of_week_monday,
            R.string.day_of_week_tuesday,
            R.string.day_of_week_wednesday,
            R.string.day_of_week_thursday,
            R.string.day_of_week_friday,
            R.string.day_of_week_saturday,
            R.string.day_of_week_sunday
        )

        return daysOfWeek[DayOfWeek.values().indexOf(dayOfWeek)]
    }
}