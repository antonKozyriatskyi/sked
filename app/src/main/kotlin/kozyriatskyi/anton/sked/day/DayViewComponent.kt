package kozyriatskyi.anton.sked.day

import dagger.BindsInstance
import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.Day
import java.time.LocalDate

@Day
@Subcomponent(modules = [DayViewModule::class])
interface DayViewComponent {


    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance date: LocalDate): DayViewComponent
    }

    fun inject(fragment: DayViewFragment)
}