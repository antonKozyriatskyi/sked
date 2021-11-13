package kozyriatskyi.anton.sked.main

import androidx.viewpager2.widget.ViewPager2

fun interface TabsOwner {

    fun setupWithViewPager(viewPager: ViewPager2, titleProvider: TitleProvider)

    fun interface TitleProvider {

        fun getTitle(position: Int): String
    }
}