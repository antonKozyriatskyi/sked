package kozyriatskyi.anton.sked.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.navigation.NavController
import kozyriatskyi.anton.sked.audiences.AudiencesComponent
import kozyriatskyi.anton.sked.audiences.AudiencesFragment
import kozyriatskyi.anton.sked.audiences.DaggerAudiencesComponent
import kozyriatskyi.anton.sked.byday.ByDayViewFragment
import kozyriatskyi.anton.sked.byday.DaggerByDayViewComponent
import kozyriatskyi.anton.sked.byweek.ByWeekViewFragment
import kozyriatskyi.anton.sked.byweek.DaggerByWeekViewComponent
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
import kozyriatskyi.anton.sked.login.student.DaggerStudentLoginComponent
import kozyriatskyi.anton.sked.login.student.StudentLoginFragment
import kozyriatskyi.anton.sked.login.teacher.DaggerTeacherLoginComponent
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginFragment
import kozyriatskyi.anton.sked.main.DaggerMainComponent
import kozyriatskyi.anton.sked.main.MainActivity
import kozyriatskyi.anton.sked.main.MainComponent
import kozyriatskyi.anton.sked.main.MainModule
import kozyriatskyi.anton.sked.schedule.DaggerScheduleComponent
import kozyriatskyi.anton.sked.schedule.ScheduleFragment
import kozyriatskyi.anton.sked.settings.DaggerSettingsComponent
import kozyriatskyi.anton.sked.settings.SettingsScreenFragment
import kozyriatskyi.anton.sked.updater.DaggerUpdaterComponent
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
        DaggerMainComponent.builder()
            .mainModule(mainModule)
            .appComponent(appComponent)
            .build()
    }

    private val mainModule: MainModule by lazy(::MainModule)

    private var audiencesComponent: AudiencesComponent? = null
        get() {
            if (field == null) {
                field = DaggerAudiencesComponent.builder()
                    .appComponent(appComponent)
                    .mainComponent(mainComponent)
                    .build()
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
        DaggerScheduleComponent.builder()
            .mainComponent(mainComponent)
            .appComponent(appComponent)
            .build()
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
        DaggerStudentLoginComponent.builder()
            .mainComponent(mainComponent)
            .appComponent(appComponent)
            .build()
            .inject(fragment)
    }

    fun inject(fragment: TeacherLoginFragment) {
        DaggerTeacherLoginComponent.builder()
            .mainComponent(mainComponent)
            .appComponent(appComponent)
            .build()
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
        DaggerByDayViewComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(fragment)
    }

    fun inject(fragment: ByWeekViewFragment) {
        DaggerByWeekViewComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(fragment)
    }

    fun inject(service: UpdaterJobService) {
        DaggerUpdaterComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(service)
    }

    fun inject(fragment: SettingsScreenFragment) {
        DaggerSettingsComponent.builder()
            .mainComponent(mainComponent)
            .appComponent(appComponent)
            .build()
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