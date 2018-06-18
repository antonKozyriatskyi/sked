package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.provider.ParsedStudentInfoProvider
import kozyriatskyi.anton.sked.data.provider.ParsedTeacherInfoProvider
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.repository.StudentInfoProvider
import kozyriatskyi.anton.sked.repository.TeacherInfoProvider
import kozyriatskyi.anton.sutparser.StudentInfoParser
import kozyriatskyi.anton.sutparser.TeacherInfoParser

@Module
class UserInfoProviderModule {

    @App
    @Provides
    fun provideStudentInfoProvider(): StudentInfoProvider = ParsedStudentInfoProvider(StudentInfoParser())

    @App
    @Provides
    fun provideTeacherInfoProvider(): TeacherInfoProvider = ParsedTeacherInfoProvider(TeacherInfoParser())
}