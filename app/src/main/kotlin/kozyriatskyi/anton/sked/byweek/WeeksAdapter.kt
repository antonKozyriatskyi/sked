package kozyriatskyi.anton.sked.byweek

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import kozyriatskyi.anton.sked.week.WeekViewFragment

class WeeksAdapter(childFragmentManager: FragmentManager, private val titles: Array<String>)
    : FragmentStatePagerAdapter(childFragmentManager) {

    companion object {
        private const val DEFAULT_TABS_COUNT = 5
    }

    override fun getItem(i: Int): Fragment = WeekViewFragment.create(i)

    override fun getCount(): Int = DEFAULT_TABS_COUNT

    override fun getPageTitle(position: Int): CharSequence = titles[position]

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
}