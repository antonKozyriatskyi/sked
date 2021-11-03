package kozyriatskyi.anton.sked.main

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.DateManipulator

/**
 * Created by Anton on 10.08.2017.
 */

@Module
class MainModule {

    @Provides
    fun provideInteractor(
        scheduleRepository: ScheduleStorage,
        mapper: LessonMapper,
        scheduleLoader: ScheduleProvider,
        userInfoStorage: UserInfoStorage,
        dateManipulator: DateManipulator
    ): MainInteractor =
        MainInteractor(scheduleRepository, mapper, scheduleLoader, userInfoStorage, dateManipulator)


    @Provides
    fun providePresenter(
        userInfoStorage: UserInfoStorage,
        userSettingsStorage: UserSettingsStorage,
        interactor: MainInteractor
    ): MainPresenter = MainPresenter(userInfoStorage, userSettingsStorage, interactor)
}