package kozyriatskyi.anton.sked.byday

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import kozyriatskyi.anton.sked.day.DayViewFragment

/**
 * Created by Anton on 04.08.2017.
 */
class DaysAdapter(childFragmentManager: FragmentManager, private val titles: Array<String>,
                  private val nextWeek: Boolean)
    : FragmentStatePagerAdapter(childFragmentManager) {

    companion object {
        private const val DEFAULT_TABS_COUNT = 5
    }

    private var count = DEFAULT_TABS_COUNT

    override fun getItem(i: Int): Fragment = DayViewFragment.create(i, nextWeek)

    override fun getCount(): Int = count

    override fun getPageTitle(position: Int): CharSequence = titles[position]

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

    fun addTab() {
        if (count < 7) {
            count++
            notifyDataSetChanged()
        }
    }

    fun removeTab() {
        if (count > 5) {
            count--
            notifyDataSetChanged()
        }
    }
}