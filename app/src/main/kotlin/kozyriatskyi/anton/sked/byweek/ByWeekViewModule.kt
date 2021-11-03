package kozyriatskyi.anton.sked.byweek

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateManipulator

@Module
class ByWeekViewModule {

    @Provides
    fun provideInteractor(
        scheduleRepository: ScheduleStorage,
        dateManipulator: DateManipulator
    ): ByWeekViewInteractor = ByWeekViewInteractor(scheduleRepository)

    @Provides
    fun providePresenter(
        interactor: ByWeekViewInteractor,
        dateManipulator: DateManipulator
    ): ByWeekViewPresenter = ByWeekViewPresenter(interactor, dateManipulator)
}