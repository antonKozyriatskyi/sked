package kozyriatskyi.anton.sked.di

import android.annotation.SuppressLint
import android.content.Context
import kozyriatskyi.anton.sked.audiences.AudiencesComponent
import kozyriatskyi.anton.sked.byday.ByDayViewComponent
import kozyriatskyi.anton.sked.byweek.ByWeekViewComponent
import kozyriatskyi.anton.sked.day.DaggerDayViewComponent
import kozyriatskyi.anton.sked.day.DayViewComponent
import kozyriatskyi.anton.sked.day.DayViewModule
import kozyriatskyi.anton.sked.di.module.AppModule
import kozyriatskyi.anton.sked.login.student.StudentLoginComponent
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginComponent
import kozyriatskyi.anton.sked.main.MainComponent
import kozyriatskyi.anton.sked.screen.schedule.ScheduleComponent
import kozyriatskyi.anton.sked.settings.SettingsScreenFragment
import kozyriatskyi.anton.sked.updater.UpdaterJobService
import kozyriatskyi.anton.sked.week.DaggerWeekViewComponent
import kozyriatskyi.anton.sked.week.WeekViewComponent
import kozyriatskyi.anton.sked.week.WeekViewModule
import java.time.LocalDate

/**
 * Created by Anton on 02.02.2018.
 */
@SuppressLint("StaticFieldLeak")
object Injector2 {

    private lateinit var appContext: Context

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(appContext))
            .build()
    }

    private var mainComponent: MainComponent? = null

    private var scheduleComponent: ScheduleComponent? = null

    fun init(appContext: Context) {
        this.appContext = appContext
    }

    fun rootComponent(): AppComponent {
        return appComponent
    }

    fun studentLoginComponent(): StudentLoginComponent {
        return appComponent.studentLoginComponent().create()
    }

    fun teacherLoginComponent(): TeacherLoginComponent {
        return appComponent.teacherLoginComponent().create()
    }

    fun mainComponent(): MainComponent {
        return appComponent.mainComponent().create().apply {
            mainComponent = this
        }
    }

    fun scheduleComponent(): ScheduleComponent {
        return mainComponent!!.scheduleComponent()
    }


    fun byDayViewComponent(): ByDayViewComponent = scheduleComponent!!.byDayViewComponent()

    fun byWeekViewComponent(): ByWeekViewComponent = scheduleComponent!!.byWeekViewComponent()

    fun audiencesComponent(): AudiencesComponent = mainComponent!!.audiencesComponent()

    fun inject(service: UpdaterJobService) {
       appComponent.updaterComponent().create().inject(service)
    }

    fun inject(fragment: SettingsScreenFragment) {
        mainComponent!!.settingsComponent().inject(fragment)
    }
}