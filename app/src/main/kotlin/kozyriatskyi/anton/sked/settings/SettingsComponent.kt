package kozyriatskyi.anton.sked.settings

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Settings
import kozyriatskyi.anton.sked.main.MainComponent

@Settings
@Component(dependencies = [AppComponent::class, MainComponent::class])
interface SettingsComponent {
    fun inject(settingsFragment: SettingsScreenFragment)
}