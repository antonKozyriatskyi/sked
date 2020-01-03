package kozyriatskyi.anton.sked.main

import androidx.viewpager.widget.ViewPager

interface TabsOwner {
    fun setupWithViewPager(viewPager: ViewPager, autoRefresh: Boolean)
}