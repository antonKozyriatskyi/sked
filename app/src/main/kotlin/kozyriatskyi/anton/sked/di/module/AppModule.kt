package kozyriatskyi.anton.sked.di.module

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.analytics.FirebaseAnalyticsManager
import kozyriatskyi.anton.sked.common.AppConfigurationManager
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.util.DateFormatter
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.JobManager

/**
 * Created by Anton on 13.07.2017.
 */

@Module(includes = [AppModule.Bindings::class])
class AppModule {

    @Provides
    fun provideJobManager(context: Context): JobManager = JobManager(context)

    @App
    @Provides
    fun provideFirebaseAnalyticsManager(context: Context): FirebaseAnalyticsManager {
        val analytics = FirebaseAnalytics.getInstance(context)
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

    @Module
    abstract class Bindings {

        @App
        @Binds
        abstract fun provideAnalyticsManager(analyticsManager: FirebaseAnalyticsManager): AnalyticsManager

    }
}

