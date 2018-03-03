package kozyriatskyi.anton.sked.byweek

import dagger.Component
import kozyriatskyi.anton.sked.di.ByWeek
import kozyriatskyi.anton.sked.di.AppComponent

@ByWeek
@Component(modules = [ByWeekViewModule::class],
        dependencies = [AppComponent::class])
interface ByWeekViewComponent {
    fun inject(fragment: ByWeekViewFragment)
}