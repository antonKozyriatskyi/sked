package kozyriatskyi.anton.sked.di.component

import dagger.Component
import kozyriatskyi.anton.sked.di.ByWeek
import kozyriatskyi.anton.sked.di.module.ByWeekViewModule
import kozyriatskyi.anton.sked.ui.fragment.ByWeekViewFragment

@ByWeek
@Component(modules = [ByWeekViewModule::class],
        dependencies = [AppComponent::class])
interface ByWeekViewComponent {
    fun inject(fragment: ByWeekViewFragment)
}