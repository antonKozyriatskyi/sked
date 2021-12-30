package kozyriatskyi.anton.sked.login.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.provider.ParsedTeacherInfoProvider
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.di.viewModel.ViewModelKey
import kozyriatskyi.anton.sked.navigation.Navigator
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.repository.TeacherInfoProvider
import kozyriatskyi.anton.sked.screen.login.teacher.TeacherLoginInteractor
import kozyriatskyi.anton.sked.screen.login.teacher.TeacherLoginViewModel
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.JobManager
import kozyriatskyi.anton.sutparser.TeacherInfoParser

@Module(includes = [TeacherLoginModule.BindingModule::class])
class TeacherLoginModule {

    @Provides
    @Login
    fun provideTeacherInfoProvider(): TeacherInfoProvider =
        ParsedTeacherInfoProvider(TeacherInfoParser())

    @Provides
    fun providePresenter(interactor: TeacherLoginInteractor, navigator: Navigator): TeacherLoginPresenter =
        TeacherLoginPresenter(interactor, navigator)

    @Login
    @Provides
    fun provideViewModel(factory: ViewModelProvider.Factory): TeacherLoginViewModel {
        return factory.create(TeacherLoginViewModel::class.java)
    }

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

    @Module
    abstract class BindingModule {

        @Binds
        @IntoMap
        @ViewModelKey(TeacherLoginViewModel::class)
        abstract fun viewModel(viewModel: TeacherLoginViewModel): ViewModel
    }
}