package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.domain.interactor.ByDayViewInteractor
import kozyriatskyi.anton.sked.domain.repository.ScheduleStorage
import kozyriatskyi.anton.sked.presentation.presenter.ByDayViewPresenter

@Module
class ByDayViewModule {

    @Provides
    fun provideInteractor(scheduleRepository: ScheduleStorage): ByDayViewInteractor =
            ByDayViewInteractor(scheduleRepository)

    @Provides
    fun providePresenter(interactor: ByDayViewInteractor): ByDayViewPresenter =
           ByDayViewPresenter(interactor)
}