package kozyriatskyi.anton.sked.byday

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import kozyriatskyi.anton.sked.day.DayViewFragment

/**
 * Created by Anton on 04.08.2017.
 */


class DaysAdapter(
    childFragmentManager: FragmentManager,
    private val context: Context
) : FragmentStatePagerAdapter(childFragmentManager) {

    private var items: List<ByDayViewItem> = emptyList()

    override fun getItem(i: Int): Fragment = DayViewFragment.create(items[i].date)

    override fun getCount(): Int = items.size

    override fun getPageTitle(position: Int): CharSequence = context.getString(items[position].title)

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

    fun update(items: List<ByDayViewItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}