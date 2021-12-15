package kozyriatskyi.anton.sked.schedule

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.ScheduleScreen
import kozyriatskyi.anton.sked.main.MainComponent

@ScheduleScreen
@Component(
    modules = [ScheduleModule::class],
    dependencies = [AppComponent::class, MainComponent::class]
)
interface ScheduleComponent {
    fun inject(fragment: ScheduleFragment)

}