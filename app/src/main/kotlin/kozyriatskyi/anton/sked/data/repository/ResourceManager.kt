package kozyriatskyi.anton.sked.data.repository

import android.content.Context
import android.support.v4.content.ContextCompat

/**
 * Created by Anton on 05.08.2017.
 */
class ResourceManager(private val context: Context) {

    fun getString(id: Int): String = context.getString(id)

    fun getString(id: Int, vararg params: Any): String = context.getString(id, params)

    fun getColor(id: Int): Int = ContextCompat.getColor(context, id)

    fun getStringArray(id: Int): Array<String> = context.resources.getStringArray(id)
}