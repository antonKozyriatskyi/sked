package kozyriatskyi.anton.sked.data.repository

import kozyriatskyi.anton.sked.R

/**
 * Created by Anton on 22.08.2017.
 */

class DateFormatter(resourceManager: ResourceManager) {

    private val daysOfWeek: Array<String> = resourceManager.getStringArray(R.array.days_of_week)

    fun dayOfWeek(position: Int): String = daysOfWeek[position]

    fun shortDate(longDate: String): String = longDate.substring(0, 5) // 01.02.2018 -> 01.02
}
