package kozyriatskyi.anton.sked.login.teacher

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.provider.ParsedTeacherInfoProvider
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.navigation.Navigator
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.repository.TeacherInfoProvider
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.JobManager
import kozyriatskyi.anton.sutparser.TeacherInfoParser

@Module
class TeacherLoginModule {

    @Provides
    @Login
    fun provideTeacherInfoProvider(): TeacherInfoProvider =
        ParsedTeacherInfoProvider(TeacherInfoParser())

    @Provides
    fun providePresenter(interactor: TeacherLoginInteractor, navigator: Navigator): TeacherLoginPresenter =
        TeacherLoginPresenter(interactor, navigator)

    @Provides
    @Login
    fun provideInteractor(
        teacherInfoProvider: TeacherInfoProvider,
        userUserInfoStorage: UserInfoStorage,
        teacherNetworkScheduleLoader: ScheduleProvider,
        scheduleRepository: ScheduleStorage,
        connectionStateProvider: ConnectionStateProvider,
        mapper: LessonMapper,
        jobManager: JobManager,
        analyticsManager: AnalyticsManager,
        dateManipulator: DateManipulator
    ): TeacherLoginInteractor = TeacherLoginInteractor(
        teacherInfoProvider,
        userUserInfoStorage,
        teacherNetworkScheduleLoader,
        scheduleRepository,
        connectionStateProvider,
        mapper,
        jobManager,
        analyticsManager,
        dateManipulator
    )
}