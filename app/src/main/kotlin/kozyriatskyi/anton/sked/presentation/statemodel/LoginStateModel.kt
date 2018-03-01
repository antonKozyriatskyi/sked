package kozyriatskyi.anton.sked.presentation.statemodel

/**
 * Created by Anton on 20.07.2017.
 */
class LoginStateModel {
    var isConnectionAvailable = false
    var isLoaded = false

    val enableUi: Boolean
        get() = isConnectionAvailable and isLoaded

    val showNoConnectionMessage: Boolean
        get() = isConnectionAvailable.not()
}