package kozyriatskyi.anton.sked.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.transition.AutoTransition
import androidx.transition.Fade
import androidx.transition.TransitionSet
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.about.AboutActivity
import kozyriatskyi.anton.sked.audiences.AudiencesActivity
import kozyriatskyi.anton.sked.byday.ByDayViewFragment
import kozyriatskyi.anton.sked.byweek.ByWeekViewFragment
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.intro.IntroActivity
import kozyriatskyi.anton.sked.settings.SettingsActivity
import kozyriatskyi.anton.sked.util.toast
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


class MainActivity : MvpAppCompatActivity(), MainView, TabsOwner,
        BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val KEY_SHOW_PROGRESS = "show_progress"

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var userInfoStorage: UserInfoStorage

    private lateinit var tabs: TabLayout

    @Inject
    @InjectPresenter
    internal lateinit var presenter: MainPresenter

    private lateinit var byDayViewFragment: Fragment
    private lateinit var byWeekViewFragment: Fragment

    private lateinit var menuProgressItem: MenuItem

    private var showProgressBar = false

    private var nightMode = AppCompatDelegate.getDefaultNightMode()

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        Injector.inject(this)
        return presenter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_SHOW_PROGRESS, showProgressBar)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        showProgressBar = savedInstanceState?.getBoolean(KEY_SHOW_PROGRESS) ?: false
        super.onCreate(savedInstanceState)

        checkForFirstLaunch(savedInstanceState)

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.main_toolbar))

        tabs = findViewById(R.id.main_tabs)

        findViewById<BottomNavigationView>(R.id.main_bottomnavigation_viewmodes)
                .apply { tryFixBlinking() }
                .setOnNavigationItemSelectedListener(this)

        byDayViewFragment = supportFragmentManager.findFragmentByTag(ByDayViewFragment.TAG) ?: ByDayViewFragment()
        byWeekViewFragment = supportFragmentManager.findFragmentByTag(ByWeekViewFragment.TAG) ?: ByWeekViewFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, byWeekViewFragment, ByWeekViewFragment.TAG)
                    .add(R.id.main_fragment_container, byDayViewFragment, ByDayViewFragment.TAG)
                    .commit()
        }
    }

    // When targeting api 29 bottom navigation view has weird issue with text flickering
    // when switching tabs
    private fun BottomNavigationView.tryFixBlinking() {
        runCatching {
            val menuView = getChildAt(0) as BottomNavigationMenuView
            val declaredFields = menuView::class.java.declaredFields
            val setField =  declaredFields.find { it.type == TransitionSet::class.java } ?: return

            with(setField) {
                isAccessible = true
                val transitionSet = (get(menuView) as AutoTransition).apply {
                    for (i in transitionCount downTo 0) {
                        val transition = getTransitionAt(i) as? Fade ?: continue
                        removeTransition(transition)
                    }
                }
                set(menuView, transitionSet)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val defaultNightMode = AppCompatDelegate.getDefaultNightMode()
        if (nightMode != defaultNightMode) {
            nightMode = defaultNightMode
            recreate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menuProgressItem = menu.findItem(R.id.menu_main_update)
        switchProgress(showProgressBar)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_relogin -> IntroActivity.start(this)
            R.id.main_preferences -> SettingsActivity.start(this)
            33 /*Show notification*/ -> { // only for debugging
                throw Exception("crashlytics test")
            }
            R.id.menu_main_update -> presenter.onUpdateTriggered()
            R.id.main_audiences -> AudiencesActivity.start(this)
            R.id.main_about -> AboutActivity.start(this)
        }

        return true
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.navigation_day -> presenter.onSetDayViewClick()
            R.id.navigation_week -> presenter.onSetWeekViewClick()
//            R.id.navigation_table -> presenter.onSetTableViewClick()
        }

        return true
    }

    override fun setupWithViewPager(viewPager: ViewPager, autoRefresh: Boolean) {
        tabs.setupWithViewPager(viewPager, autoRefresh)
    }

    override fun setSubtitle(text: String) {
        supportActionBar?.subtitle = text
    }

    override fun switchProgress(showProgressBar: Boolean) {
        this.showProgressBar = showProgressBar

        if (showProgressBar) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    private fun checkForFirstLaunch(savedInstanceState: Bundle?) {
        if (savedInstanceState == null && userInfoStorage.isFirstLaunch()) {
            IntroActivity.start(this)
            finish()
        }
    }

    private fun showProgress() {
        menuProgressItem.setActionView(R.layout.action_main_progress)
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
        supportFragmentManager.beginTransaction()
                .hide(byWeekViewFragment)
                .show(byDayViewFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

    override fun setWeekView() {
        supportFragmentManager.beginTransaction()
                .hide(byDayViewFragment)
                .show(byWeekViewFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

    override fun setTableView() {

    }
}
