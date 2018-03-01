package kozyriatskyi.anton.sked.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.util.FirebaseLogger
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

    @Provides
    fun provideFirebaseLogger(): FirebaseLogger = FirebaseLogger(appContext)

    @App
    @Provides
    fun provideResourceManager(context: Context): ResourceManager = ResourceManager(context)
}

