@file:Suppress("NOTHING_TO_INLINE")

package kozyriatskyi.anton.sutparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

private const val TIMEOUT = 10_000

internal fun loadDocument(url: String): Document = Jsoup.connect(url).timeout(TIMEOUT).get()

internal inline fun Document.getElements(elementId: String): List<ParsedItem> =
        this.getElementById(elementId)
                .getElementsByAttributeValueMatching("value", "[0-9]+")
                .map(::ParsedItem)
                .toList()

internal inline fun verifyAllPresentOrThrow(errorMessage: String, vararg values: String?) {
    val allPresent = values.all { it != null && it.isNotEmpty() }

    if (allPresent.not()) throw ParseException(errorMessage)
}