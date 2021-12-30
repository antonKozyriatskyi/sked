package kozyriatskyi.anton.sked.flow.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainFlowViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Schedule)
    val state: StateFlow<MainState> get() = _state

    fun goToAudiences() {
        _state.value = MainState.Audiences
    }

    fun goToSettings() {
        _state.value = MainState.Settings
    }

    fun goToAbout() {
        _state.value = MainState.About
    }
}