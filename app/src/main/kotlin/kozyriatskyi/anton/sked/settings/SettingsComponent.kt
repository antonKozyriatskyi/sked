package kozyriatskyi.anton.sked.settings

import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.Settings

@Settings
@Subcomponent
interface SettingsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }

    fun inject(settingsFragment: SettingsFragment)
}