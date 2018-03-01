package kozyriatskyi.anton.sked.adapter

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

    companion object {
        private const val TYPE_LESSON = 1
        private const val TYPE_EMPTY = 2
        private const val TYPE_HEADER = 4
    }

    private var items = ArrayList<LessonUi>(4)
    private val viewTypes = ArrayList<Int>()

    private lateinit var shortDate: String

    override fun getItemCount(): Int = viewTypes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LessonHolder -> with(holder) {
                val lesson = items[position - 1]
                name.text = lesson.name
                who.text = lesson.whoShort
                type.text = lesson.type
                type.setTextColor(lesson.typeColor)
                number.text = lesson.number
                cabinet.text = lesson.cabinet
                time.text = lesson.time
            }

            is HeaderHolder -> holder.shortDate.text = shortDate

            is EmptyHolder -> holder.noLessonsDate.text = shortDate
        }

    }

    override fun getItemViewType(position: Int): Int = viewTypes[position]

    override fun onCreateViewHolder(container: ViewGroup, type: Int): RecyclerView.ViewHolder =
            when (type) {
                DayLessonsAdapter.TYPE_LESSON ->
                    LessonHolder(container.inflate(R.layout.item_day_lesson))
                DayLessonsAdapter.TYPE_HEADER ->
                    HeaderHolder(container.inflate(R.layout.item_day_header))
                else /*TYPE_EMPTY*/ -> EmptyHolder(container.inflate(R.layout.item_empty))
            }

    fun updateData(dayUi: DayUi) {
        items.clear()
        viewTypes.clear()

        shortDate = dayUi.shortDate

        if (dayUi.lessons.isEmpty()) {
            viewTypes.add(DayLessonsAdapter.TYPE_EMPTY)
            notifyDataSetChanged()
            return
        }

        items.addAll(dayUi.lessons)
        viewTypes.add(DayLessonsAdapter.TYPE_HEADER)
        viewTypes.addAll(items.map { DayLessonsAdapter.TYPE_LESSON })
        viewTypes.trimToSize()

        notifyDataSetChanged()
    }

    private inner class LessonHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.find(R.id.lesson_name)
        val who: TextView = view.find(R.id.lesson_who)
        val type: TextView = view.find(R.id.lesson_type)
        val number: TextView = view.find(R.id.lesson_number)
        val cabinet: TextView = view.find(R.id.lesson_cabinet)
        val time: TextView = view.find(R.id.lesson_time)

        init {
            view.find<View>(R.id.lesson_card).setOnClickListener {
                onLessonClickListener.onLessonClick(items[adapterPosition - 1]) // Counting from the second position, because first position is a header view
            }
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