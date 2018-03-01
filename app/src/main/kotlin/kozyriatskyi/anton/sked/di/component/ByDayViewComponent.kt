package kozyriatskyi.anton.sked.di.component

import dagger.Component
import kozyriatskyi.anton.sked.di.ByDay
import kozyriatskyi.anton.sked.di.module.ByDayViewModule
import kozyriatskyi.anton.sked.ui.fragment.ByDayViewFragment

@ByDay
@Component(modules = [ByDayViewModule::class],
        dependencies = [AppComponent::class])
interface ByDayViewComponent {
    fun inject(fragment: ByDayViewFragment)
}