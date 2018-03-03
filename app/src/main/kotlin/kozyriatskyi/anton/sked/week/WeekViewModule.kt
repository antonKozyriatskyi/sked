package kozyriatskyi.anton.sked.week

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage

@Module
class WeekViewModule(private val weekNumber: Int) {

    @Provides
    fun provideInteractor(scheduleRepository: ScheduleStorage,
                          userInfoStorage: UserInfoStorage): WeekViewInteractor =
            WeekViewInteractor(scheduleRepository, userInfoStorage)

    @Provides
    fun providePresenter(interactor: WeekViewInteractor, dayMapper: DayMapper): WeekViewPresenter =
            WeekViewPresenter(weekNumber, interactor, dayMapper)
}