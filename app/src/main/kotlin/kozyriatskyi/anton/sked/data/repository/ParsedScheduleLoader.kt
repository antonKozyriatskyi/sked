package kozyriatskyi.anton.sked.data.repository

import dagger.Lazy
import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.Student
import kozyriatskyi.anton.sked.data.pojo.Teacher
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.StudentScheduleProvider
import kozyriatskyi.anton.sked.repository.TeacherScheduleProvider

/**
 * Created by Anton on 26.07.2017.
 */
class ParsedScheduleLoader(private val studentScheduleProvider: Lazy<StudentScheduleProvider>,
                           private val teacherScheduleProvider: Lazy<TeacherScheduleProvider>) : ScheduleProvider {

    override fun getSchedule(user: User): List<LessonNetwork> = when (user) {
        is Student -> {
            val facultyId = user.facultyId
            val courseId = user.courseId
            val groupId = user.groupId
            studentScheduleProvider.get().getSchedule(facultyId, courseId, groupId)
        }
        is Teacher -> {
            val departmentId = user.departmentId
            val teacherId = user.teacherId
            teacherScheduleProvider.get().getSchedule(departmentId, teacherId)
        }
    }
}