package kozyriatskyi.anton.sked.di

import android.content.Context
import dagger.Component
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.common.AppConfigurationManager
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.di.module.*
import kozyriatskyi.anton.sked.flow.root.RootFlowSubcomponentsModule
import kozyriatskyi.anton.sked.flow.root.RootFlowViewModel
import kozyriatskyi.anton.sked.login.student.StudentLoginComponent
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginComponent
import kozyriatskyi.anton.sked.main.MainComponent
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.settings.SettingsComponent
import kozyriatskyi.anton.sked.updater.UpdaterComponent
import kozyriatskyi.anton.sked.util.DateFormatter
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.JobManager
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger

/**
 * Created by Anton on 03.02.2018.
 */

@App
@Component(modules = [AppModule::class, StorageModule::class, MapperModule::class, ScheduleProviderModule::class, RootFlowSubcomponentsModule::class, ConnectionModule::class])
interface AppComponent {
    fun appContext(): Context
    fun jobManager(): JobManager
    fun analyticsManager(): AnalyticsManager
    fun resourceManager(): ResourceManager
    fun userInfoStorage(): UserInfoStorage
    fun userSettingsStorage(): UserSettingsStorage
    fun scheduleRepository(): ScheduleStorage
    fun scheduleLoader(): ScheduleProvider
    fun scheduleUpdateTimeLogger(): ScheduleUpdateTimeLogger
    fun lessonMapper(): LessonMapper
    fun dayMapper(): DayMapper
    fun dateManipulator(): DateManipulator
    fun dateFormatter(): DateFormatter
    fun appConfigurationManager(): AppConfigurationManager

    fun viewModel(): RootFlowViewModel

    fun studentLoginComponent(): StudentLoginComponent
    fun teacherLoginComponent(): TeacherLoginComponent
    fun updaterComponent(): UpdaterComponent
    fun mainComponent(): MainComponent
    fun settingsComponent(): SettingsComponent
}