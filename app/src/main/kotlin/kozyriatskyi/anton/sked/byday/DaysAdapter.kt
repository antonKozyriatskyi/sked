package kozyriatskyi.anton.sked.byday

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kozyriatskyi.anton.sked.day.DayViewFragment

/**
 * Created by Anton on 04.08.2017.
 */


class DaysAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var items: List<ByDayViewItem> = emptyList()

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        return DayViewFragment.create(items[position].day.date)
    }

    fun getTitle(context: Context, position: Int): String {
        return context.getString(items[position].title)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<ByDayViewItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}