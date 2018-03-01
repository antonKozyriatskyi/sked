package kozyriatskyi.anton.sked.di.component

import android.content.Context
import dagger.Component
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.di.module.*
import kozyriatskyi.anton.sked.domain.repository.ScheduleLoader
import kozyriatskyi.anton.sked.domain.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.FirebaseLogger
import kozyriatskyi.anton.sked.util.JobManager
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger

/**
 * Created by Anton on 03.02.2018.
 */

@App
@Component(modules = [AppModule::class, StorageModule::class, MapperModule::class, LoaderModule::class,
    FormatterModule::class])
interface AppComponent {
    fun appContext(): Context
    fun jobManager(): JobManager
    fun firebaseLogger(): FirebaseLogger
    fun resourceManager(): ResourceManager
    fun userInfoStorage(): UserInfoStorage
    fun userSettingsStorage(): UserSettingsStorage
    fun scheduleRepository(): ScheduleStorage
    fun scheduleLoader(): ScheduleLoader
    fun scheduleUpdateTimeLogger(): ScheduleUpdateTimeLogger
    fun lessonMapper(): LessonMapper
    fun dayMapper(): DayMapper
}