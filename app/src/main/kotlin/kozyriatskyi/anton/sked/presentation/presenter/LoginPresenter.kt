package kozyriatskyi.anton.sked.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.repository.ResourceManager
import kozyriatskyi.anton.sked.presentation.statemodel.LoginStateModel
import kozyriatskyi.anton.sked.presentation.view.LoginView
import kozyriatskyi.anton.sked.ui.activity.LoginActivity

/**
 * Created by Anton on 13.07.2017.
 */

@InjectViewState
class LoginPresenter(private val userType: LoginActivity.UserType,
                     private val resourceManager: ResourceManager) : MvpPresenter<LoginView>() {

    private val uiModel = LoginStateModel()

    override fun onFirstViewAttach() {
        when (userType) {
            LoginActivity.UserType.STUDENT -> {
                viewState.setTitle(resourceManager.getString(R.string.login_student))
                viewState.showStudentLayout()
            }
            LoginActivity.UserType.TEACHER -> {
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
}