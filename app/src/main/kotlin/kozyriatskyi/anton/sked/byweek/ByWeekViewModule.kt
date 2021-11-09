package kozyriatskyi.anton.sked.byweek

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateFormatter
import kozyriatskyi.anton.sked.util.DateManipulator

@Module
class ByWeekViewModule {

    @Provides
    fun provideInteractor(
        scheduleRepository: ScheduleStorage
    ): ByWeekViewInteractor = ByWeekViewInteractor(scheduleRepository)

    @Provides
    fun providePresenter(
        interactor: ByWeekViewInteractor,
        dateManipulator: DateManipulator,
        itemMapper: ByWeekViewItemMapper,
        userSettingsStorage: UserSettingsStorage
    ): ByWeekViewPresenter = ByWeekViewPresenter(
        interactor,
        dateManipulator,
        itemMapper,
        userSettingsStorage
    )


    @Provides
    fun provideByWeekViewItemMapper(dateFormatter: DateFormatter): ByWeekViewItemMapper {
        return ByWeekViewItemMapper(dateFormatter)
    }
}