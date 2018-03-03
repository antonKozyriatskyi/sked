package kozyriatskyi.anton.sked.settings

import dagger.Component
import kozyriatskyi.anton.sked.di.Settings
import kozyriatskyi.anton.sked.di.AppComponent

@Settings
@Component(dependencies = [AppComponent::class])
interface SettingsComponent {
    fun inject(settingsFragment: SettingsFragment)
}