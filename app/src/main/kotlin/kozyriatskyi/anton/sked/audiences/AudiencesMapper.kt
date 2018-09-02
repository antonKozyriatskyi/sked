package kozyriatskyi.anton.sked.audiences

import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork
import kozyriatskyi.anton.sked.data.repository.ResourceManager

class AudiencesMapper(private val resourceManager: ResourceManager) {

    fun networkToUi(audiences: List<AudienceNetwork>): List<AudienceUi> = audiences.map {
        AudienceUi(number = it.number,
                isFree = it.isFree,
                status = status(it.isFree),
                note = it.note,
                capacity = naIfEmpty(it.capacity),
                colorId = color(it.isFree)
        )
    }

    // na = N/A
    private fun naIfEmpty(string: String): String {
        return if (string.isEmpty() or string.isBlank()) {
            resourceManager.getString(R.string.na)
        } else {
            string
        }
    }

    private fun status(isFree: Boolean): String {
        val id = if (isFree) R.string.audience_status_free else R.string.audience_status_busy
        return resourceManager.getString(id)
    }

    private fun color(isFree: Boolean): Int {
        val id = if (isFree) R.color.audienceFree else R.color.audienceBusy
        return resourceManager.getColor(id)
    }
}