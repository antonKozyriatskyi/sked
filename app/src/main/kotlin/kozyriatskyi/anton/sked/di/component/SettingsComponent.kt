package kozyriatskyi.anton.sked.di.component

import dagger.Component
import kozyriatskyi.anton.sked.di.Settings
import kozyriatskyi.anton.sked.ui.fragment.SettingsFragment

@Settings
@Component(dependencies = [AppComponent::class])
interface SettingsComponent {
    fun inject(settingsFragment: SettingsFragment)
}