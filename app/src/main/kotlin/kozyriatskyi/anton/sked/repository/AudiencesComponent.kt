package kozyriatskyi.anton.sked.repository

import dagger.Subcomponent
import kozyriatskyi.anton.sked.audiences.AudiencesActivity
import kozyriatskyi.anton.sked.audiences.AudiencesModule
import kozyriatskyi.anton.sked.di.Audiences

@Audiences
@Subcomponent(modules = [AudiencesModule::class])
interface AudiencesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AudiencesComponent
    }

    fun inject(activity: AudiencesActivity)
}