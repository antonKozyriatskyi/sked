package kozyriatskyi.anton.sked.byday

import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.ByDay
import kozyriatskyi.anton.sked.screen.schedule.byDay.ByDayViewModel

@ByDay
@Subcomponent(modules = [ByDayViewModule::class])
interface ByDayViewComponent {

    fun inject(fragment: ByDayViewFragment)

    fun viewModel(): ByDayViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): ByDayViewComponent
    }
}