package kozyriatskyi.anton.sked.main

import androidx.navigation.NavController
import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.common.AppConfigurationManager
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.MainScreen
import kozyriatskyi.anton.sked.navigation.Navigator
import kozyriatskyi.anton.sked.util.DateManipulator

/**
 * Created by Anton on 10.08.2017.
 */

@Module
class MainModule {

    private val navigator: Navigator by lazy {
        Navigator()
    }

    @Provides
    fun provideInteractor(
        dateManipulator: DateManipulator,
        appConfigurationManager: AppConfigurationManager
    ): MainInteractor = MainInteractor(
        dateManipulator,
        appConfigurationManager
    )


    @Provides
    fun providePresenter(
        userInfoStorage: UserInfoStorage,
        interactor: MainInteractor,
        navigator: Navigator
    ): MainPresenter = MainPresenter(userInfoStorage, interactor, navigator)

    @MainScreen
    @Provides
    fun provideNavigator(): Navigator = navigator

    fun updateNavComponent(navController: NavController) {
        navigator.updateNavController(navController)
    }
}