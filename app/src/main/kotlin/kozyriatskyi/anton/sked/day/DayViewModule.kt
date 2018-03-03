package kozyriatskyi.anton.sked.day

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage

@Module
class DayViewModule(private val dayNumber: Int, private val nextWeek: Boolean) {

    @Provides
    fun provideInteractor(scheduleRepository: ScheduleStorage, userInfoStorage: UserInfoStorage): DayViewInteractor {
        return DayViewInteractor(scheduleRepository, userInfoStorage)
    }

    @Provides
    fun providePresenter(dayViewInteractor: DayViewInteractor, dayMapper: DayMapper): DayViewPresenter {
        return DayViewPresenter(dayNumber, nextWeek, dayViewInteractor, dayMapper)
    }
}