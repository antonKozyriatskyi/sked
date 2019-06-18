package kozyriatskyi.anton.sutparser

import org.jsoup.nodes.Element

/**
 * Created by Anton on 23.02.2017.
 */

/**
 * Represents a single faculty, course, group, student, department or teacher
 */
data class ParsedItem(val id: String, val value: String) {
    constructor(element: Element) : this(element.`val`(), element.text())

    override fun toString(): String = "id: $id | value: $value"
}