package kozyriatskyi.anton.sked.byweek

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.ByWeek

@ByWeek
@Component(
    modules = [ByWeekViewModule::class],
    dependencies = [AppComponent::class]
)
interface ByWeekViewComponent {
    fun inject(fragment: ByWeekViewFragment)
}