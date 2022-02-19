package kozyriatskyi.anton.sked.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.module.*
import kozyriatskyi.anton.sked.login.LoginComponent
import kozyriatskyi.anton.sked.main.MainComponent
import kozyriatskyi.anton.sked.updater.UpdaterComponent
import kozyriatskyi.anton.sked.util.JobManager

/**
 * Created by Anton on 03.02.2018.
 */

@App
@Component(
    modules = [
        AppModule::class,
        AppSubcomponents::class,
        StorageModule::class,
        MapperModule::class,
        ScheduleProviderModule::class,
        NetworkingModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun userInfoStorage(): UserInfoStorage

    fun jobManager(): JobManager

    fun loginComponent(): LoginComponent.Factory

    fun mainComponent(): MainComponent.Factory

    fun updaterComponent(): UpdaterComponent.Factory
}

@Module(subcomponents = [LoginComponent::class, MainComponent::class, UpdaterComponent::class])
class AppSubcomponents
