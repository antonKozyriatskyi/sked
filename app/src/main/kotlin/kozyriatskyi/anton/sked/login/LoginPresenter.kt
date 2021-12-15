package kozyriatskyi.anton.sked.login

import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.navigation.Navigator
import moxy.InjectViewState
import moxy.MvpPresenter

/**
 * Created by Anton on 13.07.2017.
 */

@InjectViewState
class LoginPresenter(
    private val userType: LoginView.UserType,
    private val resourceManager: ResourceManager,
    private val navigator: Navigator
) : MvpPresenter<LoginView>() {

    private val uiModel = LoginStateModel()

    override fun onFirstViewAttach() {
        when (userType) {
            LoginView.UserType.STUDENT -> {
                viewState.setTitle(resourceManager.getString(R.string.login_student))
                viewState.showStudentLayout()
            }
            LoginView.UserType.TEACHER -> {
                viewState.setTitle(resourceManager.getString(R.string.login_teacher))
                viewState.showTeacherLayout()
            }
        }
    }

    fun onInternetConnectionStateChanged(isAvailable: Boolean) {
        uiModel.isConnectionAvailable = isAvailable
        viewState.enableUi(uiModel.enableUi)
        viewState.switchNoConnectionMessage(uiModel.showNoConnectionMessage)
    }

    fun onLoadingStateChanged(isLoaded: Boolean) {
        uiModel.isLoaded = isLoaded
        viewState.enableUi(uiModel.enableUi)
    }

    fun onBackClick() {
        navigator.pop()
    }
}