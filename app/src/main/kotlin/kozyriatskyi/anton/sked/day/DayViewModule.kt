package kozyriatskyi.anton.sked.day

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import java.time.LocalDate

@Module
class DayViewModule {

    @Provides
    fun provideInteractor(scheduleRepository: ScheduleStorage, userInfoStorage: UserInfoStorage): DayViewInteractor {
        return DayViewInteractor(scheduleRepository, userInfoStorage)
    }

    @Provides
    fun providePresenter(dayViewInteractor: DayViewInteractor, dayMapper: DayMapper, date: LocalDate): DayViewPresenter {
        return DayViewPresenter(date, dayViewInteractor, dayMapper)
    }
}