package kozyriatskyi.anton.sked.week

import dagger.BindsInstance
import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.Week
import java.time.LocalDate

@Week
@Subcomponent(modules = [WeekViewModule::class])
interface WeekViewComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance dates: List<LocalDate>): WeekViewComponent
    }

    fun inject(fragment: WeekViewFragment)
}