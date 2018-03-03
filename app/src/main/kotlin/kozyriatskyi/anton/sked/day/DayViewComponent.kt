package kozyriatskyi.anton.sked.day

import dagger.Component
import kozyriatskyi.anton.sked.di.Day
import kozyriatskyi.anton.sked.di.AppComponent

@Day
@Component(modules = [DayViewModule::class],
        dependencies = [AppComponent::class])
interface DayViewComponent {
    fun inject(fragment: DayViewFragment)
}