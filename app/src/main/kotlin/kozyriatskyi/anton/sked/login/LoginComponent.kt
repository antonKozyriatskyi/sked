package kozyriatskyi.anton.sked.login

import dagger.BindsInstance
import dagger.Subcomponent
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.di.module.ConnectionModule
import kozyriatskyi.anton.sked.login.student.StudentLoginFragment
import kozyriatskyi.anton.sked.login.student.StudentLoginModule
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginFragment
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginModule

@Login
@Subcomponent(
    modules = [
        LoginModule::class,
        TeacherLoginModule::class,
        StudentLoginModule::class,
        ConnectionModule::class
    ]
)
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance userType: LoginView.UserType): LoginComponent
    }

    fun inject(activity: LoginActivity)

    fun inject(teacherFragment: TeacherLoginFragment)

    fun inject(studentLoginFragment: StudentLoginFragment)
}