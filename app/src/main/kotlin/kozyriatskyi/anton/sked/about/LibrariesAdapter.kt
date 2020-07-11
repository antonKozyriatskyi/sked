package kozyriatskyi.anton.sked.about

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.Library
import kozyriatskyi.anton.sked.util.inflate

class LibrariesAdapter(private val data: List<Library>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            LibrariesHolder(parent.inflate(R.layout.item_library))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LibrariesHolder) {
            holder.name.text = data[position].name
            holder.license.text = data[position].license
        }
    }

    private class LibrariesHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.library_name)
        val license: TextView = view.findViewById(R.id.library_license)
    }
}