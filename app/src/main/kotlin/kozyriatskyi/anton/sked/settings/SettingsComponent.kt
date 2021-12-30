package kozyriatskyi.anton.sked.settings

import dagger.Component
import dagger.Subcomponent
import kozyriatskyi.anton.sked.audiences.AudiencesComponent
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Settings
import kozyriatskyi.anton.sked.main.MainComponent

@Settings
@Subcomponent()
interface SettingsComponent {
    fun inject(settingsFragment: SettingsScreenFragment)


    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }
}