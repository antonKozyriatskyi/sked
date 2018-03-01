package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.parser.TeacherInfoParser
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.TeacherParsedInfoLoader
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.domain.interactor.TeacherLoginInteractor
import kozyriatskyi.anton.sked.domain.repository.ScheduleLoader
import kozyriatskyi.anton.sked.domain.repository.ScheduleStorage
import kozyriatskyi.anton.sked.domain.repository.TeacherInfoLoader
import kozyriatskyi.anton.sked.presentation.presenter.TeacherLoginPresenter
import kozyriatskyi.anton.sked.util.FirebaseLogger
import kozyriatskyi.anton.sked.util.JobManager

@Module
class TeacherLoginModule {

    @Provides
    @Login
    fun provideTeacherInfoProvider(): TeacherInfoLoader =
            TeacherParsedInfoLoader(TeacherInfoParser())

    @Provides
    fun providePresenter(interactor: TeacherLoginInteractor): TeacherLoginPresenter =
            TeacherLoginPresenter(interactor)

    @Provides
    @Login
    fun provideInteractor(teacherInfoProvider: TeacherInfoLoader,
                          userUserInfoStorage: UserInfoStorage,
                          teacherNetworkScheduleLoader: ScheduleLoader,
                          scheduleRepository: ScheduleStorage,
                          connectionStateProvider: ConnectionStateProvider,
                          mapper: LessonMapper, jobManager: JobManager, logger: FirebaseLogger): TeacherLoginInteractor {

        return TeacherLoginInteractor(teacherInfoProvider, userUserInfoStorage,
                teacherNetworkScheduleLoader, scheduleRepository, connectionStateProvider, mapper,
                jobManager, logger)
    }
}