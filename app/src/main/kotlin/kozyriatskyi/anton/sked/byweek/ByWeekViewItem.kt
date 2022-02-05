package kozyriatskyi.anton.sked.byweek

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class ByWeekViewItem(
    val title: String,
    val dates: List<LocalDate>
) : Parcelable