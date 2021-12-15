package kozyriatskyi.anton.sked.main

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.MainScreen
import kozyriatskyi.anton.sked.navigation.Navigator

@MainScreen
@Component(
    modules = [MainModule::class],
    dependencies = [AppComponent::class]
)
interface MainComponent {

    fun provideNavigator(): Navigator

    fun inject(activity: MainActivity)
}