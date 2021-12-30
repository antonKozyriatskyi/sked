package kozyriatskyi.anton.sked.login.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.provider.ParsedStudentInfoProvider
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.di.viewModel.ViewModelKey
import kozyriatskyi.anton.sked.navigation.Navigator
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.repository.StudentInfoProvider
import kozyriatskyi.anton.sked.screen.login.student.StudentLoginInteractor
import kozyriatskyi.anton.sked.screen.login.student.StudentLoginViewModel
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.JobManager
import kozyriatskyi.anton.sutparser.StudentInfoParser

@Module(includes = [StudentLoginModule.BindingModule::class])
class StudentLoginModule {

    @Provides
    @Login
    fun provideStudentInfoProvider(): StudentInfoProvider =
        ParsedStudentInfoProvider(StudentInfoParser())

    @Provides
    fun providePresenter(
        interactor: StudentLoginInteractor,
        navigator: Navigator
    ): StudentLoginPresenter =
        StudentLoginPresenter(interactor, navigator)

    @Login
    @Provides
    fun provideViewModel(factory: ViewModelProvider.Factory): StudentLoginViewModel {
        return factory.create(StudentLoginViewModel::class.java)
    }

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

    @Module
    abstract class BindingModule {

        @Binds
        @IntoMap
        @ViewModelKey(StudentLoginViewModel::class)
        abstract fun viewModel(viewModel: StudentLoginViewModel): ViewModel
    }
}