package kozyriatskyi.anton.sked.di.component

import dagger.Component
import kozyriatskyi.anton.sked.di.Week
import kozyriatskyi.anton.sked.di.module.WeekViewModule
import kozyriatskyi.anton.sked.ui.fragment.WeekViewFragment

@Week
@Component(modules = [WeekViewModule::class],
        dependencies = [AppComponent::class])
interface WeekViewComponent {
    fun inject(fragment: WeekViewFragment)
}