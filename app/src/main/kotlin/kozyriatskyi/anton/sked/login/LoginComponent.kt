package kozyriatskyi.anton.sked.login

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.di.module.ConnectionModule

@Login
@Component(modules = [LoginModule::class, ConnectionModule::class],
        dependencies = [AppComponent::class])
interface LoginComponent {
    fun inject(activity: LoginActivity)
}