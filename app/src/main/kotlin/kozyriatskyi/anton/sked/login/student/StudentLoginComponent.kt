package kozyriatskyi.anton.sked.login.student

import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.screen.login.student.StudentLoginViewModel

@Login
@Subcomponent(modules = [StudentLoginModule::class])
interface StudentLoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): StudentLoginComponent
    }

    fun viewModel(): StudentLoginViewModel

    fun inject(studentLoginFragment: StudentLoginFragment)
}