package kozyriatskyi.anton.sked.main

import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage

class MainInteractor(private val scheduleStorage: ScheduleStorage,
                     private val lessonMapper: LessonMapper,
                     private val scheduleLoader: ScheduleProvider,
                     private val userInfoStorage: UserInfoStorage) {

    suspend fun updateSchedule(): Result<Unit> {
        val schedule = scheduleLoader.getSchedule(userInfoStorage.getUser())
        val dbSchedule = lessonMapper.networkToDb(schedule)
        scheduleStorage.saveLessons(dbSchedule)

        return Result.success(Unit)
    }
}