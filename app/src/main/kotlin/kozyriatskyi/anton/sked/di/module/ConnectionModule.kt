package kozyriatskyi.anton.sked.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.repository.ConnectionStateProvider
import kozyriatskyi.anton.sked.di.Login

@Module
class ConnectionModule {

    @Login
    @Provides
    fun provideConnectionStateProvider(context: Context): ConnectionStateProvider =
            ConnectionStateProvider(context.applicationContext)
}