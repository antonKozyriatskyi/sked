package kozyriatskyi.anton.sked.audiences

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Audiences
import kozyriatskyi.anton.sked.main.MainComponent

@Audiences
@Component(
    modules = [AudiencesModule::class],
    dependencies = [AppComponent::class, MainComponent::class]
)
interface AudiencesComponent {
    fun inject(fragment: AudiencesFragment)
}