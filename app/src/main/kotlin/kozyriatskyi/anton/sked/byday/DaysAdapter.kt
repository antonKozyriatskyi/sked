package kozyriatskyi.anton.sked.byday

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
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