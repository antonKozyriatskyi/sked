package kozyriatskyi.anton.sked.domain.interactor

import io.reactivex.Completable
import io.reactivex.Single
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.pojo.LessonNetwork
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.domain.repository.ScheduleLoader
import kozyriatskyi.anton.sked.domain.repository.ScheduleStorage

class MainInteractor(private val scheduleStorage: ScheduleStorage,
                     private val lessonMapper: LessonMapper,
                     private val scheduleLoader: ScheduleLoader,
                     private val userInfoStorage: UserInfoStorage) {

    fun updateSchedule(): Completable {
        return Single.create<List<LessonNetwork>> {
            try {
                val schedule = scheduleLoader.getSchedule(userInfoStorage.getUser())
                it.onSuccess(schedule)
            } catch (t: Throwable) {
                it.onError(t)
            }
        }
                .map(lessonMapper::networkToDb)
                .doOnSuccess { scheduleStorage.saveLessons(it) }
                .toCompletable()
    }
}