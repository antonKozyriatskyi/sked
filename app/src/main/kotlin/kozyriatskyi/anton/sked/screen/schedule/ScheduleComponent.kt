package kozyriatskyi.anton.sked.screen.schedule

import dagger.Subcomponent
import kozyriatskyi.anton.sked.byday.ByDayViewComponent
import kozyriatskyi.anton.sked.byweek.ByWeekViewComponent
import kozyriatskyi.anton.sked.di.ScheduleScreen
import kozyriatskyi.anton.sked.schedule.ScheduleFragment

@ScheduleScreen
@Subcomponent(modules = [ScheduleModule::class, ScheduleSubcomponentsModule::class])
interface ScheduleComponent {

    fun byDayViewComponent(): ByDayViewComponent

    fun byWeekViewComponent(): ByWeekViewComponent

    fun viewModel(): ScheduleViewModel

    fun inject(fragment: ScheduleFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ScheduleComponent
    }
}