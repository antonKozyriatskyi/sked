package kozyriatskyi.anton.sked.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
    NavigationBarView.OnItemSelectedListener {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var userInfoStorage: UserInfoStorage

    @Inject
    @InjectPresenter
    internal lateinit var presenter: MainPresenter

    private lateinit var byDayViewFragment: Fragment
    private lateinit var byWeekViewFragment: Fragment

    private lateinit var menuProgressItem: MenuItem

    private lateinit var appbar: AppBarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var tabs: TabLayout
    private lateinit var bottomNav: BottomNavigationView

    private var tabLayoutMediator: TabLayoutMediator? = null

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        Injector.inject(this)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkForFirstLaunch(savedInstanceState)

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        appbar = findViewById(R.id.main_appbar)
        toolbar = findViewById(R.id.main_toolbar)
        tabs = findViewById(R.id.main_tabs)
        bottomNav = findViewById(R.id.main_bottomnavigation_viewmodes)

        findViewById<BottomNavigationView>(R.id.main_bottomnavigation_viewmodes)
            .setOnItemSelectedListener(this)

        setupMenu()

        byDayViewFragment =
            supportFragmentManager.findFragmentByTag(ByDayViewFragment.TAG) ?: ByDayViewFragment()
        byWeekViewFragment =
            supportFragmentManager.findFragmentByTag(ByWeekViewFragment.TAG) ?: ByWeekViewFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, byWeekViewFragment, ByWeekViewFragment.TAG)
                .add(R.id.main_fragment_container, byDayViewFragment, ByDayViewFragment.TAG)
                .commit()
        }

        presenter.updateLocale(ConfigurationCompat.getLocales(resources.configuration)[0])
    }

    private fun setupMenu() {
        toolbar.inflateMenu(R.menu.main_menu)
        menuProgressItem = toolbar.menu.findItem(R.id.menu_main_update)
        toolbar.setOnMenuItemClickListener(::onMenuItemSelected)
    }

    private fun onMenuItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_relogin -> IntroActivity.start(this, true)
            R.id.main_preferences -> SettingsActivity.start(this)
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

    private fun checkForFirstLaunch(savedInstanceState: Bundle?) {
        if (savedInstanceState == null && userInfoStorage.isFirstLaunch()) {
            IntroActivity.start(this, false)
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

    override fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }

        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        )
        val hasPermission = result == PackageManager.PERMISSION_GRANTED

        presenter.onNotificationPermissionChecked(hasPermission)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun requestNotificationPermission() {
        val launcher = registerForActivityResult(RequestPermission()) {
            // Do nothing
        }

        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
}
