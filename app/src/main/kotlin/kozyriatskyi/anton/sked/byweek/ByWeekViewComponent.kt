package kozyriatskyi.anton.sked.byweek

import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.ByWeek

@ByWeek
@Subcomponent(modules = [ByWeekViewModule::class])
interface ByWeekViewComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ByWeekViewComponent
    }

    fun inject(fragment: ByWeekViewFragment)
}