package kozyriatskyi.anton.sked.byweek

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import kozyriatskyi.anton.sked.week.WeekViewFragment

class WeeksAdapter(childFragmentManager: FragmentManager) : FragmentStatePagerAdapter(childFragmentManager) {

    private var items: List<WeekTabItem> = emptyList()

    override fun getItem(i: Int): Fragment = WeekViewFragment.create(items[i].dates)

    override fun getCount(): Int = items.size

    override fun getPageTitle(position: Int): CharSequence = items[position].title

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

    fun update(items: List<WeekTabItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}