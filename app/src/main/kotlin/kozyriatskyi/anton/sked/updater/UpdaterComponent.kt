package kozyriatskyi.anton.sked.updater

import dagger.Component
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Updater
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.repository.ScheduleLoader
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger

@Updater
@Component(dependencies = [AppComponent::class])
interface UpdaterComponent {
    fun inject(job: UpdaterJobService)

    fun scheduleLoader(): ScheduleLoader
    fun userInfoPreferences(): UserInfoStorage
    fun timeLogger(): ScheduleUpdateTimeLogger
}