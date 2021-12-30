package kozyriatskyi.anton.sked.login.teacher

import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.screen.login.teacher.TeacherLoginViewModel

@Login
@Subcomponent(modules = [TeacherLoginModule::class])
interface TeacherLoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TeacherLoginComponent
    }

    fun viewModel(): TeacherLoginViewModel

    fun inject(teacherFragment: TeacherLoginFragment)
}