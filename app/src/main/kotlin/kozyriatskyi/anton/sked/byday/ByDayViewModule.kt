package kozyriatskyi.anton.sked.byday

import dagger.Module
import dagger.Provides
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
        itemMapper: ByDayViewItemMapper
    ): ByDayViewPresenter = ByDayViewPresenter(interactor, dateManipulator, itemMapper)

    @Provides
    fun provideByDayViewItemMapper(): ByDayViewItemMapper = ByDayViewItemMapper()
}