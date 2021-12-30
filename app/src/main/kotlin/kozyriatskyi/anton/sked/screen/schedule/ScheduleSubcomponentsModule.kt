package kozyriatskyi.anton.sked.screen.schedule

import dagger.Module
import kozyriatskyi.anton.sked.byday.ByDayViewComponent
import kozyriatskyi.anton.sked.byweek.ByWeekViewComponent
import kozyriatskyi.anton.sked.login.student.StudentLoginModule
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginComponent
import kozyriatskyi.anton.sked.main.MainComponent

@Module(subcomponents = [
    ByWeekViewComponent::class,
    ByDayViewComponent::class,
])
class ScheduleSubcomponentsModule