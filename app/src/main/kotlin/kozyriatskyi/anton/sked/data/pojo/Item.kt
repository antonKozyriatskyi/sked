package kozyriatskyi.anton.sked.data.pojo

import org.jsoup.nodes.Element

/**
 * Created by Anton on 23.02.2017.
 */

/**
 * Represents a single faculty, course, group or student
 */
data class Item(val id: String = "", val value: String = "") {
    constructor(element: Element) : this(element.`val`(), element.text())

    override fun toString(): String = "id: $id | value: $value"
}