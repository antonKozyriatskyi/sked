package kozyriatskyi.anton.sked.login

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.util.inflate
import java.util.*

class ItemSpinnerAdapter : BaseAdapter() {

    private companion object {
        @LayoutRes const val ITEM_ID = R.layout.spinner_item
        @LayoutRes const val DROPDOWN_ITEM_ID = R.layout.spinner_dropdown_item
    }

    private var data: ArrayList<Item> = ArrayList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
            bindView(convertView, parent, ITEM_ID, position)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
            bindView(convertView, parent, DROPDOWN_ITEM_ID, position)

    private fun bindView(convertView: View?, parent: ViewGroup, @LayoutRes layoutId: Int, position: Int): View {
        val view = convertView ?: parent.inflate(layoutId)

        view.findViewById<TextView>(R.id.textView).apply {
            text = data[position].value
            isSelected = true
        }

        return view
    }

    fun getSelectedItem(position: Int): Item = data[position]

    fun updateData(newData: ArrayList<Item>) {
        data.clear()
        data.addAll(newData)

        notifyDataSetInvalidated()
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = data.size
}