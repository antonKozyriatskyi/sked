package kozyriatskyi.anton.sked.audiences

import dagger.Component
import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Audiences
import kozyriatskyi.anton.sked.main.MainComponent

@Audiences
@Subcomponent(
    modules = [AudiencesModule::class]
)
interface AudiencesComponent {
    fun inject(fragment: AudiencesFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): AudiencesComponent
    }
}