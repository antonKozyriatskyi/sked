package kozyriatskyi.anton.sked.flow.root

import dagger.Module
import kozyriatskyi.anton.sked.login.student.StudentLoginComponent
import kozyriatskyi.anton.sked.login.student.StudentLoginModule
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginComponent
import kozyriatskyi.anton.sked.main.MainComponent

@Module(subcomponents = [
    StudentLoginComponent::class,
    TeacherLoginComponent::class,
    MainComponent::class
])
class RootFlowSubcomponentsModule


