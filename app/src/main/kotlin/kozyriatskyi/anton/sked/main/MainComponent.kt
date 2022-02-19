package kozyriatskyi.anton.sked.main

import dagger.Module
import dagger.Subcomponent
import kozyriatskyi.anton.sked.byday.ByDayViewComponent
import kozyriatskyi.anton.sked.byweek.ByWeekViewComponent
import kozyriatskyi.anton.sked.day.DayViewComponent
import kozyriatskyi.anton.sked.di.MainScreen
import kozyriatskyi.anton.sked.repository.AudiencesComponent
import kozyriatskyi.anton.sked.settings.SettingsComponent
import kozyriatskyi.anton.sked.week.WeekViewComponent

@MainScreen
@Subcomponent(modules = [MainModule::class, MainSubcomponents::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun byDayViewComponent(): ByDayViewComponent.Factory

    fun byWeekViewComponent(): ByWeekViewComponent.Factory

    fun classroomsComponent(): AudiencesComponent.Factory

    fun settingsComponent(): SettingsComponent.Factory

    fun dayViewComponent(): DayViewComponent.Factory

    fun weekViewComponent(): WeekViewComponent.Factory

    fun inject(activity: MainActivity)

}

@Module(
    subcomponents = [
        ByDayViewComponent::class,
        ByWeekViewComponent::class,
        WeekViewComponent::class,
        DayViewComponent::class,
        AudiencesComponent::class,
        SettingsComponent::class,
    ]
)
private class MainSubcomponents