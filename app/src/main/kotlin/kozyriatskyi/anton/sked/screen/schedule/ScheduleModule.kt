package kozyriatskyi.anton.sked.screen.schedule

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.common.AppConfigurationManager
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.navigation.Navigator
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.schedule.SchedulePresenter
import kozyriatskyi.anton.sked.util.DateManipulator

/**
 * Created by Anton on 10.08.2017.
 */

@Module
class ScheduleModule {

    @Provides
    fun provideInteractor(
        scheduleStorage: ScheduleStorage,
        lessonMapper: LessonMapper,
        scheduleLoader: ScheduleProvider,
        userInfoStorage: UserInfoStorage,
        dateManipulator: DateManipulator,
        appConfigurationManager: AppConfigurationManager
    ): ScheduleInteractor = ScheduleInteractor(
        scheduleStorage = scheduleStorage,
        lessonMapper = lessonMapper,
        scheduleLoader = scheduleLoader,
        userInfoStorage = userInfoStorage,
        dateManipulator = dateManipulator,
        appConfigurationManager = appConfigurationManager
    )


    @Provides
    fun providePresenter(
        userInfoStorage: UserInfoStorage,
        userSettingsStorage: UserSettingsStorage,
        interactor: ScheduleInteractor,
        navigator: Navigator
    ): SchedulePresenter = SchedulePresenter(
        userInfoStorage,
        userSettingsStorage,
        interactor,
        navigator
    )
}