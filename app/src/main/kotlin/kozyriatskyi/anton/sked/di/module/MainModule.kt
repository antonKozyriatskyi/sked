package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.domain.interactor.MainInteractor
import kozyriatskyi.anton.sked.domain.repository.ScheduleLoader
import kozyriatskyi.anton.sked.domain.repository.ScheduleStorage
import kozyriatskyi.anton.sked.presentation.presenter.MainPresenter

/**
 * Created by Anton on 10.08.2017.
 */

@Module
class MainModule {

    @Provides
    fun provideInteractor(scheduleRepository: ScheduleStorage,
                          mapper: LessonMapper, scheduleLoader: ScheduleLoader,
                          userInfoStorage: UserInfoStorage): MainInteractor =
            MainInteractor(scheduleRepository, mapper, scheduleLoader, userInfoStorage)


    @Provides
    fun providePresenter(userInfoStorage: UserInfoStorage,
                         userSettingsStorage: UserSettingsStorage, interactor: MainInteractor): MainPresenter =
            MainPresenter(userInfoStorage, userSettingsStorage, interactor)
}