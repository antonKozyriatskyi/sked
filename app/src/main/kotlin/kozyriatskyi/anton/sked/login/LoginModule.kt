package kozyriatskyi.anton.sked.login

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.repository.ResourceManager

@Module
class LoginModule(private val userType: LoginView.UserType) {

    @Provides
    fun provideLoginPresenter(resourceManager: ResourceManager): LoginPresenter = LoginPresenter(userType, resourceManager)
}