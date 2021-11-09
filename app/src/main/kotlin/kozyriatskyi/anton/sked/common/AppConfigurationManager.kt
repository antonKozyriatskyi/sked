package kozyriatskyi.anton.sked.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kozyriatskyi.anton.sked.data.repository.FirstDayOfWeekMode
import java.util.*

class AppConfigurationManager {

    private val _configurationChanges: MutableStateFlow<Configuration?> = MutableStateFlow(null)
    val configurationChanges: Flow<Configuration> = _configurationChanges.filterNotNull()

    private var locale: Locale = Locale.getDefault()

    private var firstDayOfWeekMode: FirstDayOfWeekMode = FirstDayOfWeekMode.Monday

    fun updateFirstDayOfWeekMode(mode: FirstDayOfWeekMode) {
        firstDayOfWeekMode = mode

        dispatchUpdate()
    }

    fun updateLocale(locale: Locale) {
        if (this.locale == locale) return

        this.locale = locale

        if (firstDayOfWeekMode == FirstDayOfWeekMode.LocaleBased) {
            dispatchUpdate()
        }
    }

    private fun dispatchUpdate() {
        _configurationChanges.value = Configuration(locale, firstDayOfWeekMode)
    }

    data class Configuration(
        val locale: Locale,
        val firstDayOfWeekMode: FirstDayOfWeekMode
    )
}