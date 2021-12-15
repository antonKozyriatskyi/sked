package kozyriatskyi.anton.sked.byday

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.ByDay

@ByDay
@Component(
    modules = [ByDayViewModule::class],
    dependencies = [AppComponent::class]
)
interface ByDayViewComponent {
    fun inject(fragment: ByDayViewFragment)
}