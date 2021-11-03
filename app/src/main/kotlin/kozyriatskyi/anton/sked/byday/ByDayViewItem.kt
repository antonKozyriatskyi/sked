package kozyriatskyi.anton.sked.byday

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class ByDayViewItem(
    @StringRes val title: Int,
    val date: LocalDate
) : Parcelable