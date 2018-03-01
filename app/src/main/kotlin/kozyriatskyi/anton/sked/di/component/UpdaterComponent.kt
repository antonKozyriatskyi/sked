package kozyriatskyi.anton.sked.di.component

import dagger.Component
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Updater
import kozyriatskyi.anton.sked.domain.repository.ScheduleLoader
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger
import kozyriatskyi.anton.sked.util.UpdaterJobService

@Updater
@Component(dependencies = [AppComponent::class])
interface UpdaterComponent {
    fun inject(job: UpdaterJobService)

    fun scheduleLoader(): ScheduleLoader
    fun userInfoPreferences(): UserInfoStorage
    fun timeLogger(): ScheduleUpdateTimeLogger
}