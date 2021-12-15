package kozyriatskyi.anton.sked.main

import kozyriatskyi.anton.sked.common.BasePresenter
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.navigation.Destination
import kozyriatskyi.anton.sked.navigation.Navigator
import moxy.InjectViewState
import java.util.*

@InjectViewState
class MainPresenter(
    private val userInfoStorage: UserInfoStorage,
    private val interactor: MainInteractor,
    private val navigator: Navigator
) : BasePresenter<MainView>() {

    override fun onFirstViewAttach() {
        setStartDestination()
    }

    private fun setStartDestination() {
        val destination = if (userInfoStorage.hasUser().not()) {
            Destination.Intro(allowBackNavigation = false)
        } else {
            Destination.Schedule
        }

        navigator.setStartDestination(destination)
    }

    fun updateLocale(locale: Locale) {
        interactor.updateLocale(locale)
    }
}