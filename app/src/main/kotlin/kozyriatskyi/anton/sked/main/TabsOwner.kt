package kozyriatskyi.anton.sked.main

import android.support.v4.view.ViewPager

interface TabsOwner {
    fun setupWithViewPager(viewPager: ViewPager, autoRefresh: Boolean)
}