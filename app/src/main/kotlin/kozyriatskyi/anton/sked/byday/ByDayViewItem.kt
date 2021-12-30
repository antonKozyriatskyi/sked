package kozyriatskyi.anton.sked.byday

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import java.time.LocalDate

@Parcelize
data class ByDayViewItem(
    @StringRes val title: Int,
    val day: DayUi
) : Parcelable