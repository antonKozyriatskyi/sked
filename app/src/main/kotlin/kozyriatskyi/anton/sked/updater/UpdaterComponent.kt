package kozyriatskyi.anton.sked.updater

import dagger.Subcomponent
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Updater
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger

@Updater
@Subcomponent
interface UpdaterComponent {
    fun inject(job: UpdaterJobService)

    fun scheduleLoader(): ScheduleProvider
    fun userInfoPreferences(): UserInfoStorage
    fun timeLogger(): ScheduleUpdateTimeLogger

    @Subcomponent.Factory
    interface Factory {
        fun create(): UpdaterComponent
    }
}