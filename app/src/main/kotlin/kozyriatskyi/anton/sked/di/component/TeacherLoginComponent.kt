package kozyriatskyi.anton.sked.di.component

import dagger.Component
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.di.module.ConnectionModule
import kozyriatskyi.anton.sked.di.module.TeacherLoginModule
import kozyriatskyi.anton.sked.ui.fragment.TeacherLoginFragment

@Login
@Component(modules = [TeacherLoginModule::class, ConnectionModule::class],
        dependencies = [AppComponent::class])
interface TeacherLoginComponent {
    fun inject(teacherFragment: TeacherLoginFragment)
}