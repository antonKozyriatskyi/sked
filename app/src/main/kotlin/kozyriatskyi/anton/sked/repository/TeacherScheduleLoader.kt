package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork

interface TeacherScheduleLoader {
    fun getSchedule(departmentId: String, teacherId: String): List<LessonNetwork>
}