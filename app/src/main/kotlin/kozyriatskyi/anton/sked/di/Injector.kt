package kozyriatskyi.anton.sked.di

import android.annotation.SuppressLint
import android.content.Context
import kozyriatskyi.anton.sked.audiences.AudiencesActivity
import kozyriatskyi.anton.sked.byday.ByDayViewFragment
import kozyriatskyi.anton.sked.byweek.ByWeekViewFragment
import kozyriatskyi.anton.sked.day.DayViewFragment
import kozyriatskyi.anton.sked.login.LoginActivity
import kozyriatskyi.anton.sked.login.LoginComponent
import kozyriatskyi.anton.sked.login.LoginView
import kozyriatskyi.anton.sked.login.student.StudentLoginFragment
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginFragment
import kozyriatskyi.anton.sked.main.MainActivity
import kozyriatskyi.anton.sked.main.MainComponent
import kozyriatskyi.anton.sked.settings.SettingsFragment
import kozyriatskyi.anton.sked.updater.UpdaterJobService
import kozyriatskyi.anton.sked.week.WeekViewFragment
import java.time.LocalDate

/**
 * Created by Anton on 02.02.2018.
 */
@SuppressLint("StaticFieldLeak")
object Injector {

    lateinit var appComponent: AppComponent

    private var mainComponent: MainComponent? = null

    private var loginComponent: LoginComponent? = null

    fun init(context: Context) {
        appComponent = DaggerAppComponent.factory().create(context)
    }

    fun inject(activity: LoginActivity, userType: LoginView.UserType) {
        loginComponent = appComponent.loginComponent().create(userType).apply {
            inject(activity)
        }
    }

    fun inject(fragment: StudentLoginFragment) {
       loginComponent!!.inject(fragment)
    }

    fun inject(fragment: TeacherLoginFragment) {
        loginComponent!!.inject(fragment)
    }

    fun clear(activity: LoginActivity) {
        loginComponent = null
    }

    fun inject(activity: MainActivity) {
        mainComponent = appComponent.mainComponent().create().apply {
            inject(activity)
        }
    }

    fun inject(fragment: DayViewFragment, date: LocalDate) {
        mainComponent!!.dayViewComponent().create(date).inject(fragment)
    }

    fun inject(fragment: WeekViewFragment, dates: List<LocalDate>) {
        mainComponent!!.weekViewComponent().create(dates).inject(fragment)
    }

    fun inject(fragment: ByDayViewFragment) {
        mainComponent!!.byDayViewComponent().create().inject(fragment)
    }

    fun inject(fragment: ByWeekViewFragment) {
        mainComponent!!.byWeekViewComponent().create().inject(fragment)
    }

    fun inject(service: UpdaterJobService) {
        appComponent.updaterComponent().create().inject(service)
    }

    fun inject(fragment: SettingsFragment) {
        mainComponent!!.settingsComponent().create().inject(fragment)
    }

    fun inject(activity: AudiencesActivity) {
        mainComponent!!.classroomsComponent().create().inject(activity)
    }
}