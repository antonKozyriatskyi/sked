package kozyriatskyi.anton.sked.di.component

import dagger.Component
import kozyriatskyi.anton.sked.di.MainScreen
import kozyriatskyi.anton.sked.di.module.MainModule
import kozyriatskyi.anton.sked.ui.activity.MainActivity

@MainScreen
@Component(modules = [MainModule::class],
        dependencies = [AppComponent::class])
interface MainComponent {
    fun inject(activity: MainActivity)

}