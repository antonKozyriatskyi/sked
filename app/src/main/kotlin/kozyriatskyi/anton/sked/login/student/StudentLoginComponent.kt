package kozyriatskyi.anton.sked.login.student

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.di.module.ConnectionModule
import kozyriatskyi.anton.sked.main.MainComponent

@Login
@Component(
    modules = [StudentLoginModule::class, ConnectionModule::class],
    dependencies = [AppComponent::class, MainComponent::class]
)
interface StudentLoginComponent {
    fun inject(studentLoginFragment: StudentLoginFragment)
}