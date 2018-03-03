package kozyriatskyi.anton.sked.byday

import dagger.Component
import kozyriatskyi.anton.sked.di.ByDay
import kozyriatskyi.anton.sked.di.AppComponent

@ByDay
@Component(modules = [ByDayViewModule::class],
        dependencies = [AppComponent::class])
interface ByDayViewComponent {
    fun inject(fragment: ByDayViewFragment)
}