package kozyriatskyi.anton.sked.schedule

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.byday.ByDayViewFragment
import kozyriatskyi.anton.sked.byweek.ByWeekViewFragment
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.util.toast
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

/**
 * Created by Backbase R&D B.V. on 27.11.2021.
 */
class ScheduleFragment : MvpAppCompatFragment(R.layout.fragment_schedule), ScheduleView, TabsOwner,
    NavigationBarView.OnItemSelectedListener {

    @Inject
    lateinit var userInfoStorage: UserInfoStorage

    @Inject
    @InjectPresenter
    internal lateinit var presenter: SchedulePresenter

    private lateinit var byDayViewFragment: Fragment
    private lateinit var byWeekViewFragment: Fragment

    private lateinit var menuProgressItem: MenuItem

    private lateinit var appbar: AppBarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var tabs: TabLayout
    private lateinit var bottomNav: BottomNavigationView

    private var tabLayoutMediator: TabLayoutMediator? = null

    @ProvidePresenter
    fun providePresenter(): SchedulePresenter {
        Injector.inject(this)
        return presenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appbar = view.findViewById(R.id.schedule_appbar)
        toolbar = view.findViewById(R.id.schedule_toolbar)
        tabs = view.findViewById(R.id.schedule_tabs)
        bottomNav = view.findViewById(R.id.schedule_bottomnavigation_viewmodes)
        bottomNav.setOnItemSelectedListener(this)

        setupMenu()
        setupFragment()
    }

    private fun setupMenu() {
        toolbar.inflateMenu(R.menu.schedule_menu)
        menuProgressItem = toolbar.menu.findItem(R.id.menu_schedule_update)
        toolbar.setOnMenuItemClickListener(::onMenuItemSelected)
    }

    private fun setupFragment() {
        byDayViewFragment = findOrCreateFragment(ByDayViewFragment.TAG, ::ByDayViewFragment)
        byWeekViewFragment = findOrCreateFragment(ByWeekViewFragment.TAG, ::ByWeekViewFragment)

        fun FragmentTransaction.addFragmentMaybe(fragment: Fragment, tag: String): FragmentTransaction {
            if (childFragmentManager.findFragmentByTag(tag) == null) {
                add(
                    R.id.schedule_fragment_container,
                    fragment,
                    tag
                )
                hide(fragment)
            }

            return this
        }

        childFragmentManager.beginTransaction()
            .addFragmentMaybe(byDayViewFragment, ByDayViewFragment.TAG)
            .addFragmentMaybe(byWeekViewFragment, ByWeekViewFragment.TAG)
            .commit()
    }

    private inline fun findOrCreateFragment(tag: String, creator: () -> Fragment): Fragment {
        return childFragmentManager.findFragmentByTag(tag) ?: creator()
    }

    private fun onMenuItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.schedule_relogin -> presenter.onReloginClick()
            R.id.schedule_preferences -> presenter.onSettingsClick()
            R.id.menu_schedule_update -> presenter.onUpdateTriggered()
            R.id.schedule_audiences -> presenter.onAudiencesClick()
            R.id.schedule_about -> presenter.onAboutClick()
        }

        return true
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.navigation_day -> presenter.onSetDayViewClick()
            R.id.navigation_week -> presenter.onSetWeekViewClick()
        }

        return true
    }

    override fun setupWithViewPager(viewPager: ViewPager2, titleProvider: TabsOwner.TitleProvider) {
        tabLayoutMediator?.detach()
        tabLayoutMediator = TabLayoutMediator(tabs, viewPager, true) { tab, positions ->
            tab.text = titleProvider.getTitle(positions)
        }.apply {
            attach()
        }

        // Sometimes selected tab doesn't get scrolled to and appear visible on screen
        // thus we manually scroll to it
        tabs.post {
            tabs.setScrollPosition(viewPager.currentItem, 0f, true)
        }
    }

    override fun setSubtitle(text: String) {
        toolbar.subtitle = text
    }

    override fun switchProgress(showProgressBar: Boolean) {
        if (showProgressBar) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    private fun showProgress() {
        menuProgressItem.setActionView(R.layout.action_schedule_progress)
    }

    private fun hideProgress() {
        menuProgressItem.actionView = null
    }

    override fun onUpdateSucceeded() {
        toast(R.string.notification_schedule_updated_successfully)
    }

    override fun onUpdateFailed() {
        toast(R.string.notification_schedule_updated_unsuccessfully)
    }

    override fun setDayView() {
        switchFragment(hide = byWeekViewFragment, show = byDayViewFragment)
    }

    override fun setWeekView() {
        switchFragment(hide = byDayViewFragment, show = byWeekViewFragment)
    }

    private fun switchFragment(hide: Fragment, show: Fragment) {
        childFragmentManager.beginTransaction()
            .hide(hide)
            .show(show)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}