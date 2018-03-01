package kozyriatskyi.anton.sked.di.component

import dagger.Component
import kozyriatskyi.anton.sked.di.Day
import kozyriatskyi.anton.sked.di.module.DayViewModule
import kozyriatskyi.anton.sked.ui.fragment.DayViewFragment

@Day
@Component(modules = [DayViewModule::class],
        dependencies = [AppComponent::class])
interface DayViewComponent {
    fun inject(fragment: DayViewFragment)
}