package kozyriatskyi.anton.sked.main

import dagger.Subcomponent
import kozyriatskyi.anton.sked.audiences.AudiencesComponent
import kozyriatskyi.anton.sked.di.MainScreen
import kozyriatskyi.anton.sked.flow.main.MainFlowSubcomponentsModule
import kozyriatskyi.anton.sked.flow.main.MainFlowViewModel
import kozyriatskyi.anton.sked.screen.schedule.ScheduleComponent
import kozyriatskyi.anton.sked.settings.SettingsComponent

@MainScreen
@Subcomponent(modules = [MainModule::class, MainFlowSubcomponentsModule::class])
interface MainComponent {

    fun viewModel(): MainFlowViewModel

    fun scheduleComponent(): ScheduleComponent

    fun audiencesComponent(): AudiencesComponent

    fun settingsComponent(): SettingsComponent

    fun inject(activity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
}