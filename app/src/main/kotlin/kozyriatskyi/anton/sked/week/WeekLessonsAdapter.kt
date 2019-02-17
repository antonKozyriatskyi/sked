package kozyriatskyi.anton.sked.week

import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.customview.stickyheaders.StickyHeaderAdapter
import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.util.find
import kozyriatskyi.anton.sked.util.inflate

@Suppress("EqualsOrHashCode")
class WeekLessonsAdapter(private val onLessonClickListener: OnLessonClickListener) : StickyHeaderAdapter<RecyclerView.ViewHolder>() {

    private companion object {
        const val TYPE_HEADER = 1
        const val TYPE_LESSON = 2
        const val TYPE_EMPTY = 3
    }

    private var items: List<Item> = ArrayList()

    override fun getHeaderPosition(itemPosition: Int): Int {
        var itemPosition = itemPosition
        var headerPosition = itemPosition
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition
                break
            }

            itemPosition -= 1
        } while (itemPosition >= 0)

        return headerPosition
    }

    override fun getHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return HeaderHolder(parent.inflate(R.layout.item_week_day_sticky_header))
    }

    override fun bindHeaderViewHolder(holder: RecyclerView.ViewHolder, headerPosition: Int) {
        (holder as HeaderHolder).bind(items[headerPosition] as HeaderItem)
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return itemPosition != RecyclerView.NO_POSITION && items[itemPosition] is HeaderItem
    }

    override fun getItemViewType(position: Int): Int = items[position].type

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                TYPE_HEADER -> HeaderHolder(parent.inflate(R.layout.item_week_day_header))
                TYPE_LESSON -> LessonHolder(parent.inflate(R.layout.item_lesson))
                TYPE_EMPTY -> EmptyHolder(parent.inflate(R.layout.item_lessons_empty))
                else -> throw IllegalStateException("no view holder found for type $viewType")
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        @Suppress("UNCHECKED_CAST")
        (holder as BindableHolder<Item>).bind(items[position])
    }

    fun update(days: List<DayUi>) {
        val newItems = ArrayList<Item>(7)

        for (day in days) {
            newItems.add(HeaderItem(day = day.day, date = day.shortDate))

            if (day.lessons.isNotEmpty()) {
                for (lesson in day.lessons) {
                    newItems.add(LessonItem(lesson = lesson))
                }
            } else {
                newItems.add(EmptyItem(day = day.day, date = day.shortDate))
            }
        }

        val result = DiffUtil.calculateDiff(LessonsDiffCallback(items, newItems), false)
        items = newItems
        result.dispatchUpdatesTo(this)
    }

    private abstract inner class BindableHolder<T : Item>(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: T)
    }

    private inner class HeaderHolder(view: View) : BindableHolder<HeaderItem>(view) {

        private val day = view.find<TextView>(R.id.lessons_tv_day)
        private val date = view.find<TextView>(R.id.lessons_tv_date)

        override fun bind(item: HeaderItem) {
            this.day.text = item.day
            this.date.text = item.date
        }
    }

    private inner class LessonHolder(view: View) : BindableHolder<LessonItem>(view) {

        private val lessonName: TextView = view.find(R.id.lesson_name)
        private val lessonWho: TextView = view.find(R.id.lesson_who)
        private val lessonType: TextView = view.find(R.id.lesson_type)
        private val lessonNumber: TextView = view.find(R.id.lesson_number)
        private val lessonCabinet: TextView = view.find(R.id.lesson_cabinet)
        private val lessonTime: TextView = view.find(R.id.lesson_time)

        init {
            view.setOnClickListener {
                onLessonClickListener.onLessonClick((items[adapterPosition] as LessonItem).lesson)
            }
        }

        override fun bind(item: LessonItem) {
            val lesson = item.lesson

            lessonName.text = lesson.name
            lessonWho.text = lesson.who
            lessonType.text = lesson.type
            lessonType.setTextColor(ContextCompat.getColor(itemView.context, lesson.typeColorRes))
            lessonNumber.text = lesson.number
            lessonCabinet.text = lesson.cabinet
            lessonTime.text = lesson.time
        }
    }

    private inner class EmptyHolder(view: View) : BindableHolder<EmptyItem>(view) {
        override fun bind(item: EmptyItem) {}
    }

    private interface Item {

        val type: Int

        override fun equals(other: Any?): Boolean
    }

    private inner class HeaderItem(val day: String, val date: String) : Item {

        override val type: Int = TYPE_HEADER

        override fun equals(other: Any?): Boolean {
            if (other !is HeaderItem) return false

            return this.date == other.date
        }
    }

    private inner class LessonItem(val lesson: LessonUi) : Item {

        override val type: Int = TYPE_LESSON

        override fun equals(other: Any?): Boolean {
            if (other !is LessonItem) return false

            return this.lesson.name == other.lesson.name
                    && this.lesson.number == other.lesson.number
                    && this.lesson.date == other.lesson.date
        }
    }

    private inner class EmptyItem(val day: String, val date: String) : Item {

        override val type: Int = TYPE_EMPTY

        override fun equals(other: Any?): Boolean {
            if (other !is EmptyItem) return false

            return this.date == other.date
        }
    }

    private class LessonsDiffCallback(private val oldList: List<Item>,
                                      private val newList: List<Item>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            val old = oldList[oldPosition]
            val new = newList[newPosition]

            return old == new
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean =
                areItemsTheSame(oldPosition, newPosition)
    }

    interface OnLessonClickListener {
        fun onLessonClick(lesson: LessonUi)
    }
}