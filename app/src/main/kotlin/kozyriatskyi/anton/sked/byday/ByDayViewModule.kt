package kozyriatskyi.anton.sked.byday

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.repository.ScheduleStorage

@Module
class ByDayViewModule {

    @Provides
    fun provideInteractor(scheduleRepository: ScheduleStorage): ByDayViewInteractor =
            ByDayViewInteractor(scheduleRepository)

    @Provides
    fun providePresenter(interactor: ByDayViewInteractor): ByDayViewPresenter =
            ByDayViewPresenter(interactor)
}