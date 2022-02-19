package kozyriatskyi.anton.sked.updater

import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.Updater

@Updater
@Subcomponent
interface UpdaterComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): UpdaterComponent
    }

    fun inject(job: UpdaterJobService)
}