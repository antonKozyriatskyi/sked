@file:OptIn(ExperimentalPagerApi::class)

package kozyriatskyi.anton.sked.screen.schedule

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kozyriatskyi.anton.sked.BuildConfig
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.di.Injector2
import kozyriatskyi.anton.sked.screen.schedule.byDay.ByDayViewScreen
import kozyriatskyi.anton.sked.util.UnitCallback
import kozyriatskyi.anton.sked.util.rememberMutableState

@Composable
fun ScheduleScreen(
    onShowAudiencesClick: UnitCallback,
    onSettingsClick: UnitCallback,
    onAboutClick: UnitCallback,
    onReloginClick: UnitCallback,
    viewModel: ScheduleViewModel = Injector2.scheduleComponent().viewModel()
) {
    val state by viewModel.state.collectAsState()
    val screenState by rememberScheduleScreenState()

    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.updateStateResult.collect { updateSucceeded ->
            val text = if (updateSucceeded) {
                R.string.notification_schedule_updated_successfully
            } else {
                R.string.notification_schedule_updated_unsuccessfully
            }

            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = BuildConfig.VERSION_NAME) },
            )
        },
        bottomBar = {
            BottomAppBar {
                BottomNavigation {
                    BottomNavigationItem(
                        selected = state.viewMode == ScheduleScreenViewMode.Day,
                        onClick = { viewModel.showDayView() },
                        icon = {
                            Image(
                                ImageVector.vectorResource(id = R.drawable.nav_day),
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(R.string.nav_by_day_schedule)) }
                    )

                    BottomNavigationItem(
                        selected = state.viewMode == ScheduleScreenViewMode.Week,
                        onClick = { viewModel.showWeekView() },
                        icon = {
                            Image(
                                ImageVector.vectorResource(id = R.drawable.nav_week),
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(R.string.nav_by_week_schedule)) }
                    )
                }
            }
        }
    ) {
        Column {
            val pagerState = when (state.viewMode) {
                ScheduleScreenViewMode.Day -> screenState.dayPagerState
                ScheduleScreenViewMode.Week -> screenState.weekPagerState
            }

            var tabTitles by rememberMutableState(emptyList<String>())

            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                tabTitles.forEachIndexed { index, tab ->
                    Tab(
                        selected = index == pagerState.currentPage,
                        onClick = {  }
                    ) {
                        Text(text = tab)
                    }
                }
            }

            when (state.viewMode) {
                ScheduleScreenViewMode.Day -> ByDayViewScreen(
                    pagerState = pagerState,
                    titlesConsumer = { tabTitles = it },
                    onLessonClick = { }
                )
                ScheduleScreenViewMode.Week -> TODO()
            }
        }
    }
}

@Immutable
private data class ScheduleScreenContentState constructor(
    val dayPagerState: PagerState,
    val weekPagerState: PagerState,
)

@Composable
private fun rememberScheduleScreenState(): MutableState<ScheduleScreenContentState> =
    rememberMutableState(
        value = ScheduleScreenContentState(
            dayPagerState = rememberPagerState(),
            weekPagerState = rememberPagerState(),
        )
    )