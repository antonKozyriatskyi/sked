package kozyriatskyi.anton.sked.login

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.navigation.Navigator

@Module
class LoginModule(private val userType: LoginView.UserType) {

    @Provides
    fun provideLoginPresenter(
        resourceManager: ResourceManager,
        navigator: Navigator
    ): LoginPresenter = LoginPresenter(userType, resourceManager, navigator)
}