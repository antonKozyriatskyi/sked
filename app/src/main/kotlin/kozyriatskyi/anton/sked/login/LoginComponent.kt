package kozyriatskyi.anton.sked.login

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.di.module.ConnectionModule
import kozyriatskyi.anton.sked.main.MainComponent

@Login
@Component(
    modules = [LoginModule::class, ConnectionModule::class],
    dependencies = [AppComponent::class, MainComponent::class]
)
interface LoginComponent {

    fun inject(fragment: LoginFragment)
}