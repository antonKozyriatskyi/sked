package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.parser.StudentInfoParser
import kozyriatskyi.anton.sked.data.parser.StudentScheduleParser
import kozyriatskyi.anton.sked.data.parser.TeacherInfoParser
import kozyriatskyi.anton.sked.data.parser.TeacherScheduleParser

@Module
class ParserModule {

    @Provides
    fun provideStudentInfoParser(): StudentInfoParser = StudentInfoParser()

    @Provides
    fun provideTeacherInfoParser(): TeacherInfoParser = TeacherInfoParser()

    @Provides
    fun provideStudentScheduleParser(): StudentScheduleParser = StudentScheduleParser()

    @Provides
    fun provideTeacherScheduleParser(): TeacherScheduleParser = TeacherScheduleParser()
}