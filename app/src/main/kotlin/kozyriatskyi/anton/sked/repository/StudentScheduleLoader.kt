package kozyriatskyi.anton.sked.repository

import kozyriatskyi.anton.sked.data.pojo.LessonNetwork

/**
 * Created by Anton on 07.07.2017.
 */
interface StudentScheduleLoader {
    fun getSchedule(facultyId: String, courseId: String, groupId: String): List<LessonNetwork>
}

