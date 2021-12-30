package kozyriatskyi.anton.sked.di.module

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.analytics.FirebaseAnalyticsManager
import kozyriatskyi.anton.sked.common.AppConfigurationManager
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.di.Login
import kozyriatskyi.anton.sked.di.viewModel.ViewModelFactory
import kozyriatskyi.anton.sked.di.viewModel.ViewModelKey
import kozyriatskyi.anton.sked.flow.root.RootFlowViewModel
import kozyriatskyi.anton.sked.screen.login.student.StudentLoginViewModel
import kozyriatskyi.anton.sked.util.DateFormatter
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.JobManager

/**
 * Created by Anton on 13.07.2017.
 */

@Module(includes = [AppModule.BindingModule::class])
class AppModule(private val appContext: Context) {

    @App
    @Provides
    fun provideContext(): Context = appContext

    @Provides
    fun provideJobManager(): JobManager = JobManager(appContext)

    @App
    @Provides
    fun provideAnalyticsManager(): AnalyticsManager {
        val analytics = FirebaseAnalytics.getInstance(appContext)
        val crashlytics = FirebaseCrashlytics.getInstance()

        return FirebaseAnalyticsManager(analytics, crashlytics)
    }

    @App
    @Provides
    fun provideResourceManager(context: Context): ResourceManager = ResourceManager(context)

    @App
    @Provides
    fun provideDateManipulator(): DateManipulator = DateManipulator()

    @App
    @Provides
    fun provideDateFormatter(): DateFormatter = DateFormatter()

    @App
    @Provides
    fun provideAppConfigurationManager(): AppConfigurationManager = AppConfigurationManager()


    @App
    @Provides
    fun provideViewModel(factory: ViewModelProvider.Factory): RootFlowViewModel {
        return factory.create(RootFlowViewModel::class.java)
    }

    @Module
    abstract class BindingModule {

        @Binds
        abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

        @Binds
        @IntoMap
        @ViewModelKey(RootFlowViewModel::class)
        abstract fun viewModel(viewModel: RootFlowViewModel): ViewModel
    }
}

