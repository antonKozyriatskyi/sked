package kozyriatskyi.anton.sked.audiences

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.util.inflate
import java.util.*

class AudiencesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val audiences = ArrayList<AudienceUi>()

    override fun getItemCount(): Int = audiences.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            AudienceHolder(parent.inflate(R.layout.item_audience))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AudienceHolder).bind(audiences[position])
    }

    fun setData(audiences: List<AudienceUi>) {
        this.audiences.clear()
        this.audiences.addAll(audiences)

        notifyDataSetChanged()
    }

    private inner class AudienceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val number = itemView.findViewById<TextView>(R.id.audience_number)
        private val note = itemView.findViewById<TextView>(R.id.audience_note)
        private val status = itemView.findViewById<ImageView>(R.id.audience_status)
        private val capacity = itemView.findViewById<TextView>(R.id.audience_capacity)

        fun bind(audience: AudienceUi) {
            number.text = audience.number
            capacity.text = audience.capacity
            note.text = audience.note
            status.setImageResource(audience.iconId)
        }
    }
}