package kozyriatskyi.anton.sked.main

import dagger.Component
import kozyriatskyi.anton.sked.di.MainScreen
import kozyriatskyi.anton.sked.di.AppComponent

@MainScreen
@Component(modules = [MainModule::class],
        dependencies = [AppComponent::class])
interface MainComponent {
    fun inject(activity: MainActivity)

}