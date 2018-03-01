package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.parser.StudentInfoParser
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.StudentParsedInfoLoader
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.domain.interactor.StudentLoginInteractor
import kozyriatskyi.anton.sked.domain.repository.ScheduleLoader
import kozyriatskyi.anton.sked.domain.repository.ScheduleStorage
import kozyriatskyi.anton.sked.domain.repository.StudentInfoLoader
import kozyriatskyi.anton.sked.presentation.presenter.StudentLoginPresenter
import kozyriatskyi.anton.sked.util.FirebaseLogger
import kozyriatskyi.anton.sked.util.JobManager

@Module
class StudentLoginModule {

    @Provides
    @Login
    fun provideStudentInfoProvider(): StudentInfoLoader =
            StudentParsedInfoLoader(StudentInfoParser())

    @Provides
    fun providePresenter(interactor: StudentLoginInteractor): StudentLoginPresenter =
            StudentLoginPresenter(interactor)

    @Provides
    @Login
    fun provideInteractor(studentInfoProvider: StudentInfoLoader,
                          userUserInfoStorage: UserInfoStorage,
                          studentNetworkScheduleLoader: ScheduleLoader,
                          scheduleRepository: ScheduleStorage,
                          connectionStateProvider: ConnectionStateProvider,
                          mapper: LessonMapper, jobManager: JobManager,
                          logger: FirebaseLogger): StudentLoginInteractor {

        return StudentLoginInteractor(studentInfoProvider, userUserInfoStorage,
                studentNetworkScheduleLoader, scheduleRepository, connectionStateProvider, mapper,
                jobManager, logger)
    }
}