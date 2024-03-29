package kozyriatskyi.anton.sked.week

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import java.time.LocalDate

@Module
class WeekViewModule {

    @Provides
    fun provideInteractor(
        scheduleRepository: ScheduleStorage,
        userInfoStorage: UserInfoStorage
    ): WeekViewInteractor = WeekViewInteractor(scheduleRepository, userInfoStorage)

    @Provides
    fun providePresenter(
        interactor: WeekViewInteractor,
        dayMapper: DayMapper,
        dates: List<LocalDate>
    ): WeekViewPresenter = WeekViewPresenter(dates, interactor, dayMapper)
}