package kozyriatskyi.anton.sked.login.student

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.provider.ParsedStudentInfoProvider
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.repository.StudentInfoProvider
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.JobManager
import kozyriatskyi.anton.sutparser.StudentInfoParser

@Module
class StudentLoginModule {

    @Provides
    @Login
    fun provideStudentInfoProvider(): StudentInfoProvider =
        ParsedStudentInfoProvider(StudentInfoParser())

    @Provides
    fun providePresenter(interactor: StudentLoginInteractor): StudentLoginPresenter =
        StudentLoginPresenter(interactor)

    @Provides
    @Login
    fun provideInteractor(
        studentInfoProvider: StudentInfoProvider,
        userUserInfoStorage: UserInfoStorage,
        scheduleProvider: ScheduleProvider,
        scheduleRepository: ScheduleStorage,
        connectionStateProvider: ConnectionStateProvider,
        mapper: LessonMapper, jobManager: JobManager,
        analyticsManager: AnalyticsManager,
        dateManipulator: DateManipulator
    ): StudentLoginInteractor = StudentLoginInteractor(
        studentInfoProvider,
        userUserInfoStorage,
        scheduleProvider,
        scheduleRepository,
        connectionStateProvider,
        mapper,
        jobManager,
        analyticsManager,
        dateManipulator
    )
}