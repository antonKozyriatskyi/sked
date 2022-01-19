package kozyriatskyi.anton.sked.audiences

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.analytics.AnalyticsManager
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.di.Audiences
import kozyriatskyi.anton.sked.repository.AudiencesProvider
import kozyriatskyi.anton.sutparser.AudiencesParser

@Module
class AudiencesModule {

    @Audiences
    @Provides
    fun providePresenter(interactor: AudiencesInteractor, mapper: AudiencesMapper): AudiencesPresenter =
            AudiencesPresenter(interactor, mapper)


    @Audiences
    @Provides
    fun provideInteractor(
        provider: AudiencesProvider,
        analyticsManager: AnalyticsManager
    ): AudiencesInteractor = AudiencesInteractor(provider, analyticsManager)

    @Audiences
    @Provides
    fun provideMapper(resourceManager: ResourceManager): AudiencesMapper =
            AudiencesMapper(resourceManager)

    @Audiences
    @Provides
    fun provideAudiencesProvider(parser: AudiencesParser): AudiencesProvider =
            ParsedAudiencesProvider(parser)
//        return FakeAudiencesProvider()

    @Audiences
    @Provides
    fun provideAudiencesParser(): AudiencesParser = AudiencesParser()
}