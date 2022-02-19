package kozyriatskyi.anton.sked.byday

import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.ByDay

@ByDay
@Subcomponent(modules = [ByDayViewModule::class])
interface ByDayViewComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ByDayViewComponent
    }

    fun inject(fragment: ByDayViewFragment)
}