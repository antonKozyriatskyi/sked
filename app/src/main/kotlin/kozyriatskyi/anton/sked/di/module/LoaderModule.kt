package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.parser.StudentScheduleParser
import kozyriatskyi.anton.sked.data.parser.TeacherScheduleParser
import kozyriatskyi.anton.sked.data.repository.NetworkScheduleLoader
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.repository.ScheduleLoader
import kozyriatskyi.anton.sked.repository.StudentScheduleLoader
import kozyriatskyi.anton.sked.repository.TeacherScheduleLoader

/**
 * Created by Anton on 26.07.2017.
 */

@Module
class LoaderModule {

    @Provides
    @App
    fun provideNetworkStudentScheduleParser(): StudentScheduleParser = StudentScheduleParser()

    @Provides
    @App
    fun provideNetworkTeacherScheduleParser(): TeacherScheduleParser = TeacherScheduleParser()

    @Provides
    @App
    fun provideNetworkStudentScheduleLoader(): StudentScheduleLoader = StudentScheduleParser()

    @Provides
    @App
    fun provideNetworkTeacherScheduleLoader(): TeacherScheduleLoader = TeacherScheduleParser()


    @Provides
    @App
    fun provideScheduleLoader(studentScheduleLoader: StudentScheduleLoader,
                              teacherScheduleLoader: TeacherScheduleLoader): ScheduleLoader {
        return NetworkScheduleLoader(studentScheduleLoader, teacherScheduleLoader)
    }

//    @Provides
//    @Singleton
//    fun provideNetworkScheduleLoader(studentScheduleLoader: StudentScheduleLoader,
//                                     teacherScheduleLoader: TeacherScheduleLoader): ScheduleLoader
//            = FakeScheduleLoader()
}