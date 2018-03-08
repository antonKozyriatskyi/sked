package kozyriatskyi.anton.sked.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
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

        setTheme(R.style.AppTheme_NoActionBar)
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
            33 /*Show notification*/ -> {
                // only for debugging
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

                val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT)

                val notificationManager = NotificationManagerCompat.from(this)

                val contentTextId = if (true) R.string.notification_schedule_updated_successfully
                else R.string.notification_schedule_updated_unsuccessfully


                //TODO remove this
                val chId = "ch_1"
                val vibrationPattern = longArrayOf(300L, 300L, 300L, 300L)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //TODO extract to resources
                    val chName = "Sked channel"
                    val description = "Channel for all Sked's notifications"
                    val importance = NotificationManagerCompat.IMPORTANCE_DEFAULT

                    val channel = NotificationChannel(chId, chName, importance)
                    channel.description = description
                    channel.enableLights(true)
                    channel.lightColor = ContextCompat.getColor(this, R.color.primary)
                    channel.enableVibration(true)
                    channel.vibrationPattern = vibrationPattern

                    val notificationManager1 = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager1.createNotificationChannel(channel)
                }

                val builder = NotificationCompat.Builder(this, chId)
                        .setContentTitle(getString(R.string.notification_schedule_updated_title))
                        .setContentText(getString(contentTextId))
                        .setSmallIcon(R.drawable.ic_notif_update)
                        .setColor(ContextCompat.getColor(this, R.color.primary))
                        .setAutoCancel(true)
                        .setVibrate(vibrationPattern)
                        .setContentIntent(pendingIntent)

                notificationManager.notify(1, builder.build())
            }

            R.id.menu_main_update -> presenter.onUpdateTriggered()

            R.id.main_about -> startActivity(Intent(this, AboutActivity::class.java))
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
