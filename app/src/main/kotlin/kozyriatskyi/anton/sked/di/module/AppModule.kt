package kozyriatskyi.anton.sked.di.module

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.analytics.FirebaseAnalyticsManager
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.util.DateFormatter
import kozyriatskyi.anton.sked.util.DateManipulator
import kozyriatskyi.anton.sked.util.JobManager

/**
 * Created by Anton on 13.07.2017.
 */

@Module
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

}

