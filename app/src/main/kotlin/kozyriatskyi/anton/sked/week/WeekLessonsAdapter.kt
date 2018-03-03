package kozyriatskyi.anton.sked.week

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

class WeekLessonsAdapter(private val onLessonClickListener: OnLessonClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_EMPTY = 0
        private const val TYPE_LESSONS_ONE = 1
        private const val TYPE_LESSONS_TWO = 2
        private const val TYPE_LESSONS_THREE = 3
        private const val TYPE_LESSONS_FOUR = 4
        private const val TYPE_LESSONS_FIVE = 5
        private const val TYPE_LESSONS_SIX = 6
        private const val TYPE_ERROR = 6
    }

    private val items = ArrayList<DayUi>(5)

    override fun getItemViewType(position: Int): Int = items[position].lessons.size

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                TYPE_LESSONS_ONE -> OneLessonHolder(parent.inflate(R.layout.item_lessons_one))
                TYPE_LESSONS_TWO -> TwoLessonsHolder(parent.inflate(R.layout.item_lessons_two))
                TYPE_LESSONS_THREE -> ThreeLessonsHolder(parent.inflate(R.layout.item_lessons_three))
                TYPE_LESSONS_FOUR -> FourLessonsHolder(parent.inflate(R.layout.item_lessons_four))
                TYPE_LESSONS_FIVE -> FiveLessonsHolder(parent.inflate(R.layout.item_lessons_five))
                TYPE_LESSONS_SIX -> SixLessonsHolder(parent.inflate(R.layout.item_lessons_six))
                TYPE_EMPTY -> EmptyHolder(parent.inflate(R.layout.item_lessons_empty))
                else /*TYPE_ERROR*/ -> ErrorHolder(parent.inflate(R.layout.item_lessons_error))
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BindableHolder) {
            holder.bind(items[position])
        }
    }

    fun update(days: List<DayUi>) {
        items.clear()
        items.addAll(days)
        items.trimToSize()

        notifyDataSetChanged()
    }

    private inner class OneLessonHolder(view: View) : BindableHolder(view) {

        private val day = view.find<TextView>(R.id.lessons_tv_day)
        private val date = view.find<TextView>(R.id.lessons_tv_date)

        private val firstLessonName: TextView = view.find(R.id.lesson_one_name)
        private val firstLessonWho: TextView = view.find(R.id.lesson_one_who)
        private val firstLessonType: TextView = view.find(R.id.lesson_one_type)
        private val firstLessonNumber: TextView = view.find(R.id.lesson_one_number)
        private val firstLessonCabinet: TextView = view.find(R.id.lesson_one_cabinet)
        private val firstLessonTime: TextView = view.find(R.id.lesson_one_time)

        override fun bind(dayUi: DayUi) {

            day.text = dayUi.day
            date.text = dayUi.shortDate

            val lesson = dayUi.lessons[0]

            firstLessonName.text = lesson.name
            firstLessonWho.text = lesson.who
            firstLessonType.text = lesson.type
            firstLessonType.setTextColor(lesson.typeColor)
            firstLessonNumber.text = lesson.number
            firstLessonCabinet.text = lesson.cabinet
            firstLessonTime.text = lesson.time
        }
    }

    private inner class TwoLessonsHolder(view: View) : BindableHolder(view) {

        private val day = view.find<TextView>(R.id.lessons_tv_day)
        private val date = view.find<TextView>(R.id.lessons_tv_date)

        private val firstLessonName: TextView = view.find(R.id.lesson_one_name)
        private val firstLessonWho: TextView = view.find(R.id.lesson_one_who)
        private val firstLessonType: TextView = view.find(R.id.lesson_one_type)
        private val firstLessonNumber: TextView = view.find(R.id.lesson_one_number)
        private val firstLessonCabinet: TextView = view.find(R.id.lesson_one_cabinet)
        private val firstLessonTime: TextView = view.find(R.id.lesson_one_time)

        private val secondLessonName: TextView = view.find(R.id.lesson_two_name)
        private val secondLessonWho: TextView = view.find(R.id.lesson_two_who)
        private val secondLessonType: TextView = view.find(R.id.lesson_two_type)
        private val secondLessonNumber: TextView = view.find(R.id.lesson_two_number)
        private val secondLessonCabinet: TextView = view.find(R.id.lesson_two_cabinet)
        private val secondLessonTime: TextView = view.find(R.id.lesson_two_time)

        override fun bind(dayUi: DayUi) {
            day.text = dayUi.day
            date.text = dayUi.shortDate

            val lesson1 = dayUi.lessons[0]

            firstLessonName.text = lesson1.name
            firstLessonWho.text = lesson1.who
            firstLessonType.text = lesson1.type
            firstLessonType.setTextColor(lesson1.typeColor)
            firstLessonNumber.text = lesson1.number
            firstLessonCabinet.text = lesson1.cabinet
            firstLessonTime.text = lesson1.time

            val lesson2 = dayUi.lessons[1]

            secondLessonName.text = lesson2.name
            secondLessonWho.text = lesson2.who
            secondLessonType.text = lesson2.type
            secondLessonType.setTextColor(lesson2.typeColor)
            secondLessonNumber.text = lesson2.number
            secondLessonCabinet.text = lesson2.cabinet
            secondLessonTime.text = lesson2.time
        }
    }

    private inner class ThreeLessonsHolder(view: View) : BindableHolder(view) {

        private val day = view.find<TextView>(R.id.lessons_tv_day)
        private val date = view.find<TextView>(R.id.lessons_tv_date)

        private val firstLessonName: TextView = view.find(R.id.lesson_one_name)
        private val firstLessonWho: TextView = view.find(R.id.lesson_one_who)
        private val firstLessonType: TextView = view.find(R.id.lesson_one_type)
        private val firstLessonNumber: TextView = view.find(R.id.lesson_one_number)
        private val firstLessonCabinet: TextView = view.find(R.id.lesson_one_cabinet)
        private val firstLessonTime: TextView = view.find(R.id.lesson_one_time)

        private val secondLessonName: TextView = view.find(R.id.lesson_two_name)
        private val secondLessonWho: TextView = view.find(R.id.lesson_two_who)
        private val secondLessonType: TextView = view.find(R.id.lesson_two_type)
        private val secondLessonNumber: TextView = view.find(R.id.lesson_two_number)
        private val secondLessonCabinet: TextView = view.find(R.id.lesson_two_cabinet)
        private val secondLessonTime: TextView = view.find(R.id.lesson_two_time)

        private val thirdLessonName: TextView = view.find(R.id.lesson_three_name)
        private val thirdLessonWho: TextView = view.find(R.id.lesson_three_who)
        private val thirdLessonType: TextView = view.find(R.id.lesson_three_type)
        private val thirdLessonNumber: TextView = view.find(R.id.lesson_three_number)
        private val thirdLessonCabinet: TextView = view.find(R.id.lesson_three_cabinet)
        private val thirdLessonTime: TextView = view.find(R.id.lesson_three_time)

        override fun bind(dayUi: DayUi) {
            day.text = dayUi.day
            date.text = dayUi.shortDate

            val lesson1 = dayUi.lessons[0]

            firstLessonName.text = lesson1.name
            firstLessonWho.text = lesson1.who
            firstLessonType.text = lesson1.type
            firstLessonType.setTextColor(lesson1.typeColor)
            firstLessonNumber.text = lesson1.number
            firstLessonCabinet.text = lesson1.cabinet
            firstLessonTime.text = lesson1.time

            val lesson2 = dayUi.lessons[1]

            secondLessonName.text = lesson2.name
            secondLessonWho.text = lesson2.who
            secondLessonType.text = lesson2.type
            secondLessonType.setTextColor(lesson2.typeColor)
            secondLessonNumber.text = lesson2.number
            secondLessonCabinet.text = lesson2.cabinet
            secondLessonTime.text = lesson2.time

            val lesson3 = dayUi.lessons[2]

            thirdLessonName.text = lesson3.name
            thirdLessonWho.text = lesson3.who
            thirdLessonType.text = lesson3.type
            thirdLessonType.setTextColor(lesson3.typeColor)
            thirdLessonNumber.text = lesson3.number
            thirdLessonCabinet.text = lesson3.cabinet
            thirdLessonTime.text = lesson3.time
        }
    }

    private inner class FourLessonsHolder(view: View) : BindableHolder(view) {

        private val day = view.find<TextView>(R.id.lessons_tv_day)
        private val date = view.find<TextView>(R.id.lessons_tv_date)

        private val firstLessonName: TextView = view.find(R.id.lesson_one_name)
        private val firstLessonWho: TextView = view.find(R.id.lesson_one_who)
        private val firstLessonType: TextView = view.find(R.id.lesson_one_type)
        private val firstLessonNumber: TextView = view.find(R.id.lesson_one_number)
        private val firstLessonCabinet: TextView = view.find(R.id.lesson_one_cabinet)
        private val firstLessonTime: TextView = view.find(R.id.lesson_one_time)

        private val secondLessonName: TextView = view.find(R.id.lesson_two_name)
        private val secondLessonWho: TextView = view.find(R.id.lesson_two_who)
        private val secondLessonType: TextView = view.find(R.id.lesson_two_type)
        private val secondLessonNumber: TextView = view.find(R.id.lesson_two_number)
        private val secondLessonCabinet: TextView = view.find(R.id.lesson_two_cabinet)
        private val secondLessonTime: TextView = view.find(R.id.lesson_two_time)

        private val thirdLessonName: TextView = view.find(R.id.lesson_three_name)
        private val thirdLessonWho: TextView = view.find(R.id.lesson_three_who)
        private val thirdLessonType: TextView = view.find(R.id.lesson_three_type)
        private val thirdLessonNumber: TextView = view.find(R.id.lesson_three_number)
        private val thirdLessonCabinet: TextView = view.find(R.id.lesson_three_cabinet)
        private val thirdLessonTime: TextView = view.find(R.id.lesson_three_time)

        private val fourthLessonName: TextView = view.find(R.id.lesson_four_name)
        private val fourthLessonWho: TextView = view.find(R.id.lesson_four_who)
        private val fourthLessonType: TextView = view.find(R.id.lesson_four_type)
        private val fourthLessonNumber: TextView = view.find(R.id.lesson_four_number)
        private val fourthLessonCabinet: TextView = view.find(R.id.lesson_four_cabinet)
        private val fourthLessonTime: TextView = view.find(R.id.lesson_four_time)

        override fun bind(dayUi: DayUi) {
            day.text = dayUi.day
            date.text = dayUi.shortDate

            val lesson1 = dayUi.lessons[0]

            firstLessonName.text = lesson1.name
            firstLessonWho.text = lesson1.who
            firstLessonType.text = lesson1.type
            firstLessonType.setTextColor(lesson1.typeColor)
            firstLessonNumber.text = lesson1.number
            firstLessonCabinet.text = lesson1.cabinet
            firstLessonTime.text = lesson1.time

            val lesson2 = dayUi.lessons[1]

            secondLessonName.text = lesson2.name
            secondLessonWho.text = lesson2.who
            secondLessonType.text = lesson2.type
            secondLessonType.setTextColor(lesson2.typeColor)
            secondLessonNumber.text = lesson2.number
            secondLessonCabinet.text = lesson2.cabinet
            secondLessonTime.text = lesson2.time

            val lesson3 = dayUi.lessons[2]

            thirdLessonName.text = lesson3.name
            thirdLessonWho.text = lesson3.who
            thirdLessonType.text = lesson3.type
            thirdLessonType.setTextColor(lesson3.typeColor)
            thirdLessonNumber.text = lesson3.number
            thirdLessonCabinet.text = lesson3.cabinet
            thirdLessonTime.text = lesson3.time

            val lesson4 = dayUi.lessons[3]

            fourthLessonName.text = lesson4.name
            fourthLessonWho.text = lesson4.who
            fourthLessonType.text = lesson4.type
            fourthLessonType.setTextColor(lesson4.typeColor)
            fourthLessonNumber.text = lesson4.number
            fourthLessonCabinet.text = lesson4.cabinet
            fourthLessonTime.text = lesson4.time
        }
    }

    private inner class FiveLessonsHolder(view: View) : BindableHolder(view) {

        private val day = view.find<TextView>(R.id.lessons_tv_day)
        private val date = view.find<TextView>(R.id.lessons_tv_date)

        private val firstLessonName: TextView = view.find(R.id.lesson_one_name)
        private val firstLessonWho: TextView = view.find(R.id.lesson_one_who)
        private val firstLessonType: TextView = view.find(R.id.lesson_one_type)
        private val firstLessonNumber: TextView = view.find(R.id.lesson_one_number)
        private val firstLessonCabinet: TextView = view.find(R.id.lesson_one_cabinet)
        private val firstLessonTime: TextView = view.find(R.id.lesson_one_time)

        private val secondLessonName: TextView = view.find(R.id.lesson_two_name)
        private val secondLessonWho: TextView = view.find(R.id.lesson_two_who)
        private val secondLessonType: TextView = view.find(R.id.lesson_two_type)
        private val secondLessonNumber: TextView = view.find(R.id.lesson_two_number)
        private val secondLessonCabinet: TextView = view.find(R.id.lesson_two_cabinet)
        private val secondLessonTime: TextView = view.find(R.id.lesson_two_time)

        private val thirdLessonName: TextView = view.find(R.id.lesson_three_name)
        private val thirdLessonWho: TextView = view.find(R.id.lesson_three_who)
        private val thirdLessonType: TextView = view.find(R.id.lesson_three_type)
        private val thirdLessonNumber: TextView = view.find(R.id.lesson_three_number)
        private val thirdLessonCabinet: TextView = view.find(R.id.lesson_three_cabinet)
        private val thirdLessonTime: TextView = view.find(R.id.lesson_three_time)

        private val fourthLessonName: TextView = view.find(R.id.lesson_four_name)
        private val fourthLessonWho: TextView = view.find(R.id.lesson_four_who)
        private val fourthLessonType: TextView = view.find(R.id.lesson_four_type)
        private val fourthLessonNumber: TextView = view.find(R.id.lesson_four_number)
        private val fourthLessonCabinet: TextView = view.find(R.id.lesson_four_cabinet)
        private val fourthLessonTime: TextView = view.find(R.id.lesson_four_time)

        private val fifthLessonName: TextView = view.find(R.id.lesson_five_name)
        private val fifthLessonWho: TextView = view.find(R.id.lesson_five_who)
        private val fifthLessonType: TextView = view.find(R.id.lesson_five_type)
        private val fifthLessonNumber: TextView = view.find(R.id.lesson_five_number)
        private val fifthLessonCabinet: TextView = view.find(R.id.lesson_five_cabinet)
        private val fifthLessonTime: TextView = view.find(R.id.lesson_five_time)

        override fun bind(dayUi: DayUi) {

            day.text = dayUi.day
            date.text = dayUi.shortDate

            val lesson1 = dayUi.lessons[0]

            firstLessonName.text = lesson1.name
            firstLessonWho.text = lesson1.who
            firstLessonType.text = lesson1.type
            firstLessonType.setTextColor(lesson1.typeColor)
            firstLessonNumber.text = lesson1.number
            firstLessonCabinet.text = lesson1.cabinet
            firstLessonTime.text = lesson1.time

            val lesson2 = dayUi.lessons[1]

            secondLessonName.text = lesson2.name
            secondLessonWho.text = lesson2.who
            secondLessonType.text = lesson2.type
            secondLessonType.setTextColor(lesson2.typeColor)
            secondLessonNumber.text = lesson2.number
            secondLessonCabinet.text = lesson2.cabinet
            secondLessonTime.text = lesson2.time

            val lesson3 = dayUi.lessons[2]

            thirdLessonName.text = lesson3.name
            thirdLessonWho.text = lesson3.who
            thirdLessonType.text = lesson3.type
            thirdLessonType.setTextColor(lesson3.typeColor)
            thirdLessonNumber.text = lesson3.number
            thirdLessonCabinet.text = lesson3.cabinet
            thirdLessonTime.text = lesson3.time

            val lesson4 = dayUi.lessons[3]

            fourthLessonName.text = lesson4.name
            fourthLessonWho.text = lesson4.who
            fourthLessonType.text = lesson4.type
            fourthLessonType.setTextColor(lesson4.typeColor)
            fourthLessonNumber.text = lesson4.number
            fourthLessonCabinet.text = lesson4.cabinet
            fourthLessonTime.text = lesson4.time

            val lesson5 = dayUi.lessons[4]

            fifthLessonName.text = lesson5.name
            fifthLessonWho.text = lesson5.who
            fifthLessonType.text = lesson5.type
            fifthLessonType.setTextColor(lesson5.typeColor)
            fifthLessonNumber.text = lesson5.number
            fifthLessonCabinet.text = lesson5.cabinet
            fifthLessonTime.text = lesson5.time
        }
    }

    private inner class SixLessonsHolder(view: View) : BindableHolder(view) {

        private val day = view.find<TextView>(R.id.lessons_tv_day)
        private val date = view.find<TextView>(R.id.lessons_tv_date)

        private val firstLessonName: TextView = view.find(R.id.lesson_one_name)
        private val firstLessonWho: TextView = view.find(R.id.lesson_one_who)
        private val firstLessonType: TextView = view.find(R.id.lesson_one_type)
        private val firstLessonNumber: TextView = view.find(R.id.lesson_one_number)
        private val firstLessonCabinet: TextView = view.find(R.id.lesson_one_cabinet)
        private val firstLessonTime: TextView = view.find(R.id.lesson_one_time)

        private val secondLessonName: TextView = view.find(R.id.lesson_two_name)
        private val secondLessonWho: TextView = view.find(R.id.lesson_two_who)
        private val secondLessonType: TextView = view.find(R.id.lesson_two_type)
        private val secondLessonNumber: TextView = view.find(R.id.lesson_two_number)
        private val secondLessonCabinet: TextView = view.find(R.id.lesson_two_cabinet)
        private val secondLessonTime: TextView = view.find(R.id.lesson_two_time)

        private val thirdLessonName: TextView = view.find(R.id.lesson_three_name)
        private val thirdLessonWho: TextView = view.find(R.id.lesson_three_who)
        private val thirdLessonType: TextView = view.find(R.id.lesson_three_type)
        private val thirdLessonNumber: TextView = view.find(R.id.lesson_three_number)
        private val thirdLessonCabinet: TextView = view.find(R.id.lesson_three_cabinet)
        private val thirdLessonTime: TextView = view.find(R.id.lesson_three_time)

        private val fourthLessonName: TextView = view.find(R.id.lesson_four_name)
        private val fourthLessonWho: TextView = view.find(R.id.lesson_four_who)
        private val fourthLessonType: TextView = view.find(R.id.lesson_four_type)
        private val fourthLessonNumber: TextView = view.find(R.id.lesson_four_number)
        private val fourthLessonCabinet: TextView = view.find(R.id.lesson_four_cabinet)
        private val fourthLessonTime: TextView = view.find(R.id.lesson_four_time)

        private val fifthLessonName: TextView = view.find(R.id.lesson_five_name)
        private val fifthLessonWho: TextView = view.find(R.id.lesson_five_who)
        private val fifthLessonType: TextView = view.find(R.id.lesson_five_type)
        private val fifthLessonNumber: TextView = view.find(R.id.lesson_five_number)
        private val fifthLessonCabinet: TextView = view.find(R.id.lesson_five_cabinet)
        private val fifthLessonTime: TextView = view.find(R.id.lesson_five_time)

        private val sixLessonName: TextView = view.find(R.id.lesson_five_name)
        private val sixLessonWho: TextView = view.find(R.id.lesson_five_who)
        private val sixLessonType: TextView = view.find(R.id.lesson_five_type)
        private val sixLessonNumber: TextView = view.find(R.id.lesson_five_number)
        private val sixLessonCabinet: TextView = view.find(R.id.lesson_five_cabinet)
        private val sixLessonTime: TextView = view.find(R.id.lesson_five_time)

        override fun bind(dayUi: DayUi) {

            day.text = dayUi.day
            date.text = dayUi.shortDate

            val lesson1 = dayUi.lessons[0]

            firstLessonName.text = lesson1.name
            firstLessonWho.text = lesson1.who
            firstLessonType.text = lesson1.type
            firstLessonType.setTextColor(lesson1.typeColor)
            firstLessonNumber.text = lesson1.number
            firstLessonCabinet.text = lesson1.cabinet
            firstLessonTime.text = lesson1.time

            val lesson2 = dayUi.lessons[1]

            secondLessonName.text = lesson2.name
            secondLessonWho.text = lesson2.who
            secondLessonType.text = lesson2.type
            secondLessonType.setTextColor(lesson2.typeColor)
            secondLessonNumber.text = lesson2.number
            secondLessonCabinet.text = lesson2.cabinet
            secondLessonTime.text = lesson2.time

            val lesson3 = dayUi.lessons[2]

            thirdLessonName.text = lesson3.name
            thirdLessonWho.text = lesson3.who
            thirdLessonType.text = lesson3.type
            thirdLessonType.setTextColor(lesson3.typeColor)
            thirdLessonNumber.text = lesson3.number
            thirdLessonCabinet.text = lesson3.cabinet
            thirdLessonTime.text = lesson3.time

            val lesson4 = dayUi.lessons[3]

            fourthLessonName.text = lesson4.name
            fourthLessonWho.text = lesson4.who
            fourthLessonType.text = lesson4.type
            fourthLessonType.setTextColor(lesson4.typeColor)
            fourthLessonNumber.text = lesson4.number
            fourthLessonCabinet.text = lesson4.cabinet
            fourthLessonTime.text = lesson4.time

            val lesson5 = dayUi.lessons[4]

            fifthLessonName.text = lesson5.name
            fifthLessonWho.text = lesson5.who
            fifthLessonType.text = lesson5.type
            fifthLessonType.setTextColor(lesson5.typeColor)
            fifthLessonNumber.text = lesson5.number
            fifthLessonCabinet.text = lesson5.cabinet
            fifthLessonTime.text = lesson5.time

            val lesson6 = dayUi.lessons[5]

            sixLessonName.text = lesson6.name
            sixLessonWho.text = lesson6.who
            sixLessonType.text = lesson6.type
            sixLessonType.setTextColor(lesson6.typeColor)
            sixLessonNumber.text = lesson6.number
            sixLessonCabinet.text = lesson6.cabinet
            sixLessonTime.text = lesson6.time
        }
    }

    private inner class EmptyHolder(view: View) : BindableHolder(view) {
        val day = view.find<TextView>(R.id.lessons_empty_tv_day)
        val date = view.find<TextView>(R.id.lessons_empty_tv_date)

        override fun bind(dayUi: DayUi) {
            day.text = dayUi.day
            date.text = dayUi.shortDate
        }
    }

    private inner class ErrorHolder(view: View) : BindableHolder(view) {
        private val day = view.find<TextView>(R.id.lessons_error_tv_day)
        private val date = view.find<TextView>(R.id.lessons_error_tv_date)

        override fun bind(dayUi: DayUi) {
            day.text = dayUi.day
            date.text = dayUi.shortDate
        }
    }

    private abstract inner class BindableHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(dayUi: DayUi)
    }

    interface OnLessonClickListener {
        fun onLessonClick(lesson: LessonUi)
    }
}