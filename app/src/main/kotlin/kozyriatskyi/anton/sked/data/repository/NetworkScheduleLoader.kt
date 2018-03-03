package kozyriatskyi.anton.sked.data.repository

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.Student
import kozyriatskyi.anton.sked.data.pojo.Teacher
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.repository.ScheduleLoader
import kozyriatskyi.anton.sked.repository.StudentScheduleLoader
import kozyriatskyi.anton.sked.repository.TeacherScheduleLoader

/**
 * Created by Anton on 26.07.2017.
 */
class NetworkScheduleLoader(private val studentScheduleLoader: StudentScheduleLoader,
                            private val teacherScheduleLoader: TeacherScheduleLoader) : ScheduleLoader {

    override fun getSchedule(user: User): List<LessonNetwork> = when (user) {
        is Student -> {
            val facultyId = user.facultyId
            val courseId = user.courseId
            val groupId = user.groupId
            studentScheduleLoader.getSchedule(facultyId, courseId, groupId)
        }
        is Teacher -> {
            val departmentId = user.departmentId
            val teacherId = user.teacherId
            teacherScheduleLoader.getSchedule(departmentId, teacherId)
        }
    }
}