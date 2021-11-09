package kozyriatskyi.anton.sked.byday

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateManipulator

@Module
class ByDayViewModule {

    @Provides
    fun provideInteractor(scheduleRepository: ScheduleStorage): ByDayViewInteractor =
        ByDayViewInteractor(scheduleRepository)

    @Provides
    fun providePresenter(
        interactor: ByDayViewInteractor,
        dateManipulator: DateManipulator,
        itemMapper: ByDayViewItemMapper,
        userSettingsStorage: UserSettingsStorage
    ): ByDayViewPresenter = ByDayViewPresenter(
        interactor,
        dateManipulator,
        itemMapper,
        userSettingsStorage
    )

    @Provides
    fun provideByDayViewItemMapper(): ByDayViewItemMapper = ByDayViewItemMapper()
}