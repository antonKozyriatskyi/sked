package kozyriatskyi.anton.sked.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.Item
import java.util.*

class SpinnerAdapter : BaseAdapter() {

    private var data: ArrayList<Item> = ArrayList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) view = LayoutInflater.from(parent.context)
                .inflate(R.layout.spinner_dropdown_item, parent, false)
        val textView = view as TextView
        textView.text = data[position].value
        textView.isSelected = true
        return textView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
//          view = LayoutInflater.from(context)
//                  .inflate(R.layout.spinner_dropdown_item, parent, false)
        }

        view!!.isSelected = true
        (view as TextView).text = data[position].value

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