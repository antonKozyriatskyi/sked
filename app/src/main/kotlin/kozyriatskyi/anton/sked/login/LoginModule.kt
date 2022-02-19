package kozyriatskyi.anton.sked.login

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.repository.ResourceManager

@Module
class LoginModule {

    @Provides
    fun provideLoginPresenter(resourceManager: ResourceManager, userType: LoginView.UserType): LoginPresenter = LoginPresenter(userType, resourceManager)
}