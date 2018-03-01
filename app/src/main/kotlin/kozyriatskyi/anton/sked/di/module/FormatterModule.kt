package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.repository.DateFormatter
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.di.App

/**
 * Created by Anton on 22.08.2017.
 */

@Module
class FormatterModule {

    @App
    @Provides
    fun provideDateFormatter(resourceManager: ResourceManager): DateFormatter = DateFormatter(resourceManager)
}