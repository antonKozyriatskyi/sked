package kozyriatskyi.anton.sked.di

import android.annotation.SuppressLint
import android.content.Context
import kozyriatskyi.anton.sked.di.component.*
import kozyriatskyi.anton.sked.di.module.AppModule
import kozyriatskyi.anton.sked.di.module.DayViewModule
import kozyriatskyi.anton.sked.di.module.WeekViewModule

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