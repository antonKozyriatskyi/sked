package kozyriatskyi.anton.sked.data.repository

import dagger.Lazy
import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.pojo.Student
import kozyriatskyi.anton.sked.data.pojo.Teacher
import kozyriatskyi.anton.sked.data.pojo.User
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.StudentScheduleProvider
import kozyriatskyi.anton.sked.repository.TeacherScheduleProvider
import java.time.LocalDate

/**
 * Created by Anton on 26.07.2017.
 */
class ParsedScheduleLoader(
    studentScheduleProvider: Lazy<StudentScheduleProvider>,
    teacherScheduleProvider: Lazy<TeacherScheduleProvider>
) : ScheduleProvider {

    private val studentScheduleProvider: StudentScheduleProvider by lazy {
        studentScheduleProvider.get()
    }

    private val teacherScheduleProvider: TeacherScheduleProvider by lazy {
        teacherScheduleProvider.get()
    }

    override suspend fun getSchedule(
        user: User,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<LessonNetwork> = when (user) {
        is Student -> {
            val facultyId = user.facultyId
            val courseId = user.courseId
            val groupId = user.groupId
            studentScheduleProvider.getSchedule(facultyId, courseId, groupId, startDate, endDate)
        }
        is Teacher -> {
            val departmentId = user.departmentId
            val teacherId = user.teacherId
            teacherScheduleProvider.getSchedule(departmentId, teacherId, startDate, endDate)
        }
    }
}