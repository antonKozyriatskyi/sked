package kozyriatskyi.anton.sked.main

import kozyriatskyi.anton.sked.common.AppConfigurationManager
import kozyriatskyi.anton.sked.util.DateManipulator
import java.util.*

class MainInteractor(
    private val dateManipulator: DateManipulator,
    private val appConfigurationManager: AppConfigurationManager
) {

    fun updateLocale(locale: Locale) {
        dateManipulator.updateLocale(locale)
        appConfigurationManager.updateLocale(locale)
    }
}