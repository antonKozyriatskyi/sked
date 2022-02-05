package kozyriatskyi.anton.sked.di.module

import dagger.Lazy
import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.api.StudentApi
import kozyriatskyi.anton.sked.data.api.TeacherApi
import kozyriatskyi.anton.sked.data.provider.ApiStudentScheduleProvider
import kozyriatskyi.anton.sked.data.provider.ApiTeacherScheduleProvider
import kozyriatskyi.anton.sked.data.repository.CombinedScheduleLoader
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.repository.ScheduleProvider
import kozyriatskyi.anton.sked.repository.StudentScheduleProvider
import kozyriatskyi.anton.sked.repository.TeacherScheduleProvider
import kozyriatskyi.anton.sked.util.DateFormatter

/**
 * Created by Anton on 26.07.2017.
 */

@Module
class ScheduleProviderModule {

    @App
    @Provides
    fun provideNetworkStudentScheduleLoader(
        api: StudentApi,
        dateFormatter: DateFormatter
    ): StudentScheduleProvider = ApiStudentScheduleProvider(api, dateFormatter)

    @App
    @Provides
    fun provideNetworkTeacherScheduleLoader(
        api: TeacherApi,
        dateFormatter: DateFormatter
    ): TeacherScheduleProvider = ApiTeacherScheduleProvider(api, dateFormatter)

    @App
    @Provides
    fun provideScheduleLoader(
        studentScheduleLoader: Lazy<StudentScheduleProvider>,
        teacherScheduleLoader: Lazy<TeacherScheduleProvider>
    ): ScheduleProvider {
        return CombinedScheduleLoader(studentScheduleLoader, teacherScheduleLoader)
    }
}