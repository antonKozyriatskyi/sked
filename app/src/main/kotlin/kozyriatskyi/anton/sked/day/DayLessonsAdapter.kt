package kozyriatskyi.anton.sked.day

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.util.find
import kozyriatskyi.anton.sked.util.inflate
import java.util.*

/**
 * Created by Anton on 05.08.2017.
 */

class DayLessonsAdapter(private val onLessonClickListener: OnLessonClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        const val TYPE_LESSON = 1
        const val TYPE_EMPTY = 2
        const val TYPE_HEADER = 3
    }

    private var items = ArrayList<LessonUi>(4)
    private val viewTypes = ArrayList<Int>()

    private lateinit var shortDate: String

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LessonHolder -> holder.bind(items[position - 1])
            is HeaderHolder -> holder.shortDate.text = shortDate
            is EmptyHolder -> holder.noLessonsDate.text = shortDate
        }
    }

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    override fun onCreateViewHolder(container: ViewGroup, type: Int): RecyclerView.ViewHolder =
            when (type) {
                TYPE_LESSON -> LessonHolder(container.inflate(R.layout.item_lesson))
                TYPE_HEADER -> HeaderHolder(container.inflate(R.layout.item_day_header))
                TYPE_EMPTY -> EmptyHolder(container.inflate(R.layout.item_empty))
                else -> throw IllegalStateException("no view holder found for type $type")
            }

    fun updateData(dayUi: DayUi) {
        items.clear()
        viewTypes.clear()

        shortDate = dayUi.shortDate

        if (dayUi.lessons.isEmpty()) {
            viewTypes.add(TYPE_EMPTY)
            notifyDataSetChanged()
            return
        }

        items.addAll(dayUi.lessons)
        viewTypes.ensureCapacity(items.size + 1)
        viewTypes.add(TYPE_HEADER)
        viewTypes.addAll(items.map { TYPE_LESSON })

        notifyDataSetChanged()
    }

    private inner class LessonHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.find(R.id.lesson_name)
        private val who: TextView = view.find(R.id.lesson_who)
        private val type: TextView = view.find(R.id.lesson_type)
        private val number: TextView = view.find(R.id.lesson_number)
        private val cabinet: TextView = view.find(R.id.lesson_cabinet)
        private val time: TextView = view.find(R.id.lesson_time)

        init {
            view.find<View>(R.id.lesson_card).setOnClickListener {
                onLessonClickListener.onLessonClick(items[adapterPosition - 1]) // Subtracting 1, because first position is a header view
            }
        }

        fun bind(lesson: LessonUi) {
            name.text = lesson.name
            who.text = lesson.who
            type.text = lesson.type
            type.setTextColor(lesson.typeColor)
            number.text = lesson.number
            cabinet.text = lesson.cabinet
            time.text = lesson.time
        }
    }

    private inner class EmptyHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noLessonsDate: TextView = view.find(R.id.item_day_tv_no_lessons_date)
    }

    private inner class HeaderHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shortDate: TextView = view.find(R.id.day_date)
    }

    interface OnLessonClickListener {
        fun onLessonClick(lesson: LessonUi)
    }
}