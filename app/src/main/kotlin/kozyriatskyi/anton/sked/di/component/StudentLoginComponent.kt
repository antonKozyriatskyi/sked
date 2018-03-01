package kozyriatskyi.anton.sked.di.component

import dagger.Component
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.di.module.ConnectionModule
import kozyriatskyi.anton.sked.di.module.StudentLoginModule
import kozyriatskyi.anton.sked.ui.fragment.StudentLoginFragment

@Login
@Component(modules = [StudentLoginModule::class, ConnectionModule::class],
        dependencies = [AppComponent::class])
interface StudentLoginComponent {
    fun inject(studentLoginFragment: StudentLoginFragment)
}