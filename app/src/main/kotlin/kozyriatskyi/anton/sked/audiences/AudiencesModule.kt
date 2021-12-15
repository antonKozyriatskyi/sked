package kozyriatskyi.anton.sked.audiences

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.di.Audiences
import kozyriatskyi.anton.sked.navigation.Navigator
import kozyriatskyi.anton.sked.repository.AudiencesProvider
import kozyriatskyi.anton.sutparser.AudiencesParser

@Module
class AudiencesModule {

    @Audiences
    @Provides
    fun providePresenter(
        interactor: AudiencesInteractor,
        mapper: AudiencesMapper,
        navigator: Navigator
    ): AudiencesPresenter = AudiencesPresenter(interactor, mapper, navigator)


    @Audiences
    @Provides
    fun provideInteractor(provider: AudiencesProvider): AudiencesInteractor =
            AudiencesInteractor(provider)

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