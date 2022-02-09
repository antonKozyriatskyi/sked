package kozyriatskyi.anton.sked.byweek

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kozyriatskyi.anton.sked.week.WeekViewFragment

class WeeksAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var items: List<ByWeekViewItem> = emptyList()

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        return items[position].dates.first().dayOfYear.toLong()
    }

    override fun createFragment(position: Int): Fragment {
        return WeekViewFragment.create(items[position].dates)
    }

    fun getTitle(position: Int): String = items[position].title

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<ByWeekViewItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}