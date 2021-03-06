package kozyriatskyi.anton.sked.data.repository

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * Created by Anton on 05.08.2017.
 */
class ResourceManager(private val context: Context) {

    fun getString(@StringRes id: Int): String = context.getString(id)

    fun getString(@StringRes id: Int, vararg params: Any): String = context.getString(id, params)

    @ColorInt
    fun getColor(@ColorRes id: Int): Int = ContextCompat.getColor(context, id)

    fun getStringArray(id: Int): Array<String> = context.resources.getStringArray(id)
}