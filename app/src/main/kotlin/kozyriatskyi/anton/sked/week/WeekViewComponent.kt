package kozyriatskyi.anton.sked.week

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Week

@Week
@Component(modules = [WeekViewModule::class],
        dependencies = [AppComponent::class])
interface WeekViewComponent {
    fun inject(fragment: WeekViewFragment)
}