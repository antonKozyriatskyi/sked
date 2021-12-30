package kozyriatskyi.anton.sked.screen.schedule.day

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.di.Injector2
import kozyriatskyi.anton.sked.screen.schedule.LessonsList
import kozyriatskyi.anton.sked.screen.schedule.day.DayLessonsViewModel.State.*
import kozyriatskyi.anton.sked.util.ValueCallback
import kozyriatskyi.anton.sked.util.asStringRes
import java.time.LocalDate

@Composable
fun DayLessonsScreen(
    day: DayUi,
    onLessonClick: ValueCallback<LessonUi>
) {
    val lessons = day.lessons

    if (lessons.isNotEmpty()) {
        ListHeader(day)
        LessonsList(day, onLessonClick)
    } else {
        EmptyDayState(day)
    }
}

@Composable
private fun ListHeader(day: DayUi) {
    Text(text = day.name.asStringRes())
}

@Composable
private fun EmptyDayState(day: DayUi) {

}

