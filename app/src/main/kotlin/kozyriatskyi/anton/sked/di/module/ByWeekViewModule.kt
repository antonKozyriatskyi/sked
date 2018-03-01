package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.domain.interactor.ByWeekViewInteractor
import kozyriatskyi.anton.sked.domain.repository.ScheduleStorage
import kozyriatskyi.anton.sked.presentation.presenter.ByWeekViewPresenter

@Module
class ByWeekViewModule {

    @Provides
    fun provideInteractor(scheduleRepository: ScheduleStorage): ByWeekViewInteractor =
            ByWeekViewInteractor(scheduleRepository)

    @Provides
    fun providePresenter(interactor: ByWeekViewInteractor): ByWeekViewPresenter =
            ByWeekViewPresenter(interactor)
}