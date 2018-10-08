package kozyriatskyi.anton.sked.repository

import dagger.Component
import kozyriatskyi.anton.sked.audiences.AudiencesActivity
import kozyriatskyi.anton.sked.audiences.AudiencesModule
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Audiences

@Audiences
@Component(modules = [AudiencesModule::class], dependencies = [AppComponent::class])
interface AudiencesComponent {
    fun inject(activity: AudiencesActivity)
}