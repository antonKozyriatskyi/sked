package kozyriatskyi.anton.sked.byweek

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.repository.ScheduleStorage

@Module
class ByWeekViewModule {

    @Provides
    fun provideInteractor(scheduleRepository: ScheduleStorage): ByWeekViewInteractor =
            ByWeekViewInteractor(scheduleRepository)

    @Provides
    fun providePresenter(interactor: ByWeekViewInteractor): ByWeekViewPresenter =
            ByWeekViewPresenter(interactor)
}