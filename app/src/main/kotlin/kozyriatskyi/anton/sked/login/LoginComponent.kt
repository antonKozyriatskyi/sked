package kozyriatskyi.anton.sked.login

import dagger.BindsInstance
import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.di.module.ConnectionModule
import kozyriatskyi.anton.sked.login.student.StudentLoginModule
import kozyriatskyi.anton.sked.login.student.StudentLoginViewModel
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginModule
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginViewModel

@Login
@Subcomponent(
    modules = [
        TeacherLoginModule::class,
        StudentLoginModule::class,
        ConnectionModule::class
    ]
)
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance userType: LoginUserType): LoginComponent
    }

    fun studentViewModel(): StudentLoginViewModel

    fun teacherViewModel(): TeacherLoginViewModel
}