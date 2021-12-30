package kozyriatskyi.anton.sked.screen.schedule.byDay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.di.Injector2
import kozyriatskyi.anton.sked.screen.schedule.byDay.ByDayViewModel.State
import kozyriatskyi.anton.sked.screen.schedule.day.DayLessonsScreen
import kozyriatskyi.anton.sked.util.ValueCallback

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ByDayViewScreen(
    titlesConsumer: ValueCallback<List<String>>,
    pagerState: PagerState,
    onLessonClick: ValueCallback<LessonUi>,
    viewModel: ByDayViewModel = Injector2.byDayViewComponent().viewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state !is State.Data) return

    val data = state as State.Data
    titlesConsumer(data.items.map { stringResource(it.title) })

    HorizontalPager(
        state = pagerState,
        count = data.items.size
    ) { page ->
        DayLessonsScreen(
            day = data.items[page].day,
            onLessonClick = onLessonClick
        )
    }

    val dayPosition by viewModel.dayPosition.collectAsState(initial = 0)
    LaunchedEffect(viewModel) {
        pagerState.scrollToPage(dayPosition)
    }
}

