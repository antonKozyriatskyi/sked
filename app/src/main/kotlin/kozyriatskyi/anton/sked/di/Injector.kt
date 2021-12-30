package kozyriatskyi.anton.sked.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.navigation.NavController
import kozyriatskyi.anton.sked.audiences.AudiencesComponent
import kozyriatskyi.anton.sked.audiences.AudiencesFragment
import kozyriatskyi.anton.sked.byday.ByDayViewFragment
import kozyriatskyi.anton.sked.byweek.ByWeekViewFragment
import kozyriatskyi.anton.sked.day.DaggerDayViewComponent
import kozyriatskyi.anton.sked.day.DayViewFragment
import kozyriatskyi.anton.sked.day.DayViewModule
import kozyriatskyi.anton.sked.di.module.AppModule
import kozyriatskyi.anton.sked.intro.DaggerIntroComponent
import kozyriatskyi.anton.sked.intro.IntroFragment
import kozyriatskyi.anton.sked.login.DaggerLoginComponent
import kozyriatskyi.anton.sked.login.LoginFragment
import kozyriatskyi.anton.sked.login.LoginModule
import kozyriatskyi.anton.sked.login.LoginView
import kozyriatskyi.anton.sked.login.student.StudentLoginFragment
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginFragment
import kozyriatskyi.anton.sked.main.MainActivity
import kozyriatskyi.anton.sked.main.MainComponent
import kozyriatskyi.anton.sked.main.MainModule
import kozyriatskyi.anton.sked.schedule.ScheduleFragment
import kozyriatskyi.anton.sked.settings.SettingsScreenFragment
import kozyriatskyi.anton.sked.updater.UpdaterJobService
import kozyriatskyi.anton.sked.week.DaggerWeekViewComponent
import kozyriatskyi.anton.sked.week.WeekViewFragment
import kozyriatskyi.anton.sked.week.WeekViewModule
import java.time.LocalDate

/**
 * Created by Anton on 02.02.2018.
 */
@SuppressLint("StaticFieldLeak")
object Injector {

    private lateinit var appContext: Context

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(appContext))
            .build()
    }

    private val mainComponent: MainComponent by lazy {
        appComponent.mainComponent().create()
    }

    private val mainModule: MainModule by lazy(::MainModule)

    private var audiencesComponent: AudiencesComponent? = null
        get() {
            if (field == null) {
                field = mainComponent.audiencesComponent()
            }
            return field
        }

    fun init(appContext: Context) {
        this.appContext = appContext
    }

    fun update(navComponent: NavController) {
        mainModule.updateNavComponent(navComponent)
    }

    fun inject(activity: MainActivity) {
        mainComponent.inject(activity)
    }

    fun inject(fragment: ScheduleFragment) {
        mainComponent.scheduleComponent()
            .inject(fragment)
    }

    fun inject(fragment: IntroFragment) {
        DaggerIntroComponent.builder()
            .mainComponent(mainComponent)
            .appComponent(appComponent)
            .build()
            .inject(fragment)
    }

    fun inject(fragment: StudentLoginFragment) {
        appComponent.studentLoginComponent().create().inject(fragment)
    }

    fun inject(fragment: TeacherLoginFragment) {
        appComponent.teacherLoginComponent().create()
            .inject(fragment)
    }

    fun inject(fragment: DayViewFragment, date: LocalDate) {
        DaggerDayViewComponent.builder()
            .dayViewModule(DayViewModule(date))
            .appComponent(appComponent)
            .build()
            .inject(fragment)
    }

    fun inject(fragment: WeekViewFragment, dates: List<LocalDate>) {
        DaggerWeekViewComponent.builder()
            .weekViewModule(WeekViewModule(dates))
            .appComponent(appComponent)
            .build()
            .inject(fragment)
    }

    fun inject(fragment: ByDayViewFragment) {
        mainComponent.scheduleComponent().byDayViewComponent()
            .inject(fragment)
    }

    fun inject(fragment: ByWeekViewFragment) {
        mainComponent.scheduleComponent().byWeekViewComponent()
            .inject(fragment)
    }

    fun inject(service: UpdaterJobService) {
        appComponent.updaterComponent().create()
            .inject(service)
    }

    fun inject(fragment: SettingsScreenFragment) {
        mainComponent.settingsComponent()
            .inject(fragment)
    }

    fun inject(fragment: AudiencesFragment) {
        audiencesComponent!!.inject(fragment)
    }

    fun release(fragment: AudiencesFragment) {
        audiencesComponent = null
    }

    fun inject(fragment: LoginFragment, userType: LoginView.UserType) {
        DaggerLoginComponent.builder()
            .appComponent(appComponent)
            .mainComponent(mainComponent)
            .loginModule(LoginModule(userType))
            .build()
            .inject(fragment)
    }
}