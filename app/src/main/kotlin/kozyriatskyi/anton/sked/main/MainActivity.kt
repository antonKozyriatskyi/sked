package kozyriatskyi.anton.sked.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kozyriatskyi.anton.sked.BuildConfig
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.about.AboutActivity
import kozyriatskyi.anton.sked.byday.ByDayViewFragment
import kozyriatskyi.anton.sked.byweek.ByWeekViewFragment
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.di.Injector
import kozyriatskyi.anton.sked.intro.IntroActivity
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.settings.SettingsActivity
import kozyriatskyi.anton.sked.util.toast
import javax.inject.Inject
import kotlin.properties.Delegates


class MainActivity : MvpAppCompatActivity(), MainView, BottomNavigationView.OnNavigationItemSelectedListener {

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

    @Inject
    lateinit var database: ScheduleStorage //only fore debugging

    // must be accessible from fragments
    lateinit var tabs: TabLayout

    @Inject
    @InjectPresenter
    internal lateinit var presenter: MainPresenter

    private lateinit var byDayViewFragment: Fragment
    private lateinit var byWeekViewFragment: Fragment

    private lateinit var progress: ProgressBar

    private lateinit var menuProgressItem: MenuItem

    private var showProgressBar by Delegates.notNull<Boolean>()

    private var nightMode = AppCompatDelegate.getDefaultNightMode()

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        Injector.mainComponent().inject(this)
        return presenter
    }

    override fun onResume() {
        super.onResume()

        val defaultNightMode = AppCompatDelegate.getDefaultNightMode()
        if (nightMode != defaultNightMode) {
            nightMode = defaultNightMode
            recreate()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_SHOW_PROGRESS, showProgressBar)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        showProgressBar = savedInstanceState?.getBoolean(KEY_SHOW_PROGRESS) ?: false
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null && userInfoStorage.isFirstLaunch()) {
            IntroActivity.start(this)
            finish()
            return
        }

        setTheme(R.style.AppTheme_NoActionBar_Dark)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.main_toolbar))

        tabs = findViewById(R.id.main_tabs)

        findViewById<BottomNavigationView>(R.id.navigationview_main_viewmodes)
                .setOnNavigationItemSelectedListener(this)

        byDayViewFragment = supportFragmentManager.findFragmentByTag(ByDayViewFragment.TAG) ?: ByDayViewFragment()
        byWeekViewFragment = supportFragmentManager.findFragmentByTag(ByWeekViewFragment.TAG) ?: ByWeekViewFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_layout_fragment_container, byWeekViewFragment, ByWeekViewFragment.TAG)
                    .add(R.id.main_layout_fragment_container, byDayViewFragment, ByDayViewFragment.TAG)
                    .commit()
        }

        progress = findViewById(R.id.main_progress)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        menuProgressItem = menu.findItem(R.id.menu_main_update)

        if (BuildConfig.DEBUG) {
            menu.add(Menu.NONE, 33, Menu.NONE, "Notify")
        }

        switchProgress(showProgressBar)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val scheduleRxDatabase = database as ScheduleRxDatabase
        when (item.itemId) {
            R.id.main_relogin -> IntroActivity.start(this)

            R.id.main_preferences -> SettingsActivity.start(this)
            33 /*Show notification*/ -> { // only for debugging
                throw Exception("crashlytics test")
            }

            R.id.menu_main_update -> presenter.onUpdateTriggered()

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
