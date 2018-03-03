package kozyriatskyi.anton.sked.di

import android.annotation.SuppressLint
import android.content.Context
import kozyriatskyi.anton.sked.byday.ByDayViewComponent
import kozyriatskyi.anton.sked.byday.DaggerByDayViewComponent
import kozyriatskyi.anton.sked.byweek.ByWeekViewComponent
import kozyriatskyi.anton.sked.byweek.DaggerByWeekViewComponent
import kozyriatskyi.anton.sked.day.DaggerDayViewComponent
import kozyriatskyi.anton.sked.day.DayViewComponent
import kozyriatskyi.anton.sked.day.DayViewModule
import kozyriatskyi.anton.sked.di.module.AppModule
import kozyriatskyi.anton.sked.login.student.DaggerStudentLoginComponent
import kozyriatskyi.anton.sked.login.student.StudentLoginComponent
import kozyriatskyi.anton.sked.login.teacher.DaggerTeacherLoginComponent
import kozyriatskyi.anton.sked.login.teacher.TeacherLoginComponent
import kozyriatskyi.anton.sked.main.DaggerMainComponent
import kozyriatskyi.anton.sked.main.MainComponent
import kozyriatskyi.anton.sked.settings.DaggerSettingsComponent
import kozyriatskyi.anton.sked.settings.SettingsComponent
import kozyriatskyi.anton.sked.updater.DaggerUpdaterComponent
import kozyriatskyi.anton.sked.updater.UpdaterComponent
import kozyriatskyi.anton.sked.week.DaggerWeekViewComponent
import kozyriatskyi.anton.sked.week.WeekViewComponent
import kozyriatskyi.anton.sked.week.WeekViewModule

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

    fun init(appContext: Context) {
        this.appContext = appContext
    }

    fun mainComponent(): MainComponent {
        return DaggerMainComponent.builder()
                .appComponent(appComponent)
                .build()
    }

    fun studentComponent(): StudentLoginComponent {
        return DaggerStudentLoginComponent.builder()
                .appComponent(appComponent)
                .build()
    }

    fun teacherComponent(): TeacherLoginComponent {
        return DaggerTeacherLoginComponent.builder()
                .appComponent(appComponent)
                .build()
    }

    fun dayViewComponent(dayNumber: Int, nextWeek: Boolean): DayViewComponent {
        return DaggerDayViewComponent.builder()
                .dayViewModule(DayViewModule(dayNumber, nextWeek))
                .appComponent(appComponent)
                .build()
    }

    fun weekViewComponent(weekNumber: Int): WeekViewComponent {
        return DaggerWeekViewComponent.builder()
                .weekViewModule(WeekViewModule(weekNumber))
                .appComponent(appComponent)
                .build()
    }

    fun byDayViewComponent(): ByDayViewComponent {
        return DaggerByDayViewComponent.builder()
                .appComponent(appComponent)
                .build()
    }

    fun byWeekViewComponent(): ByWeekViewComponent {
        return DaggerByWeekViewComponent.builder()
                .appComponent(appComponent)
                .build()
    }

    fun updaterJobComponent(): UpdaterComponent =
            DaggerUpdaterComponent.builder()
                    .appComponent(appComponent)
                    .build()

    fun settingsComponent(): SettingsComponent =
            DaggerSettingsComponent.builder()
                    .appComponent(appComponent)
                    .build()
}