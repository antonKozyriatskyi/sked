package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.util.DateFormatter

@Module
class MapperModule {

    @App
    @Provides
    fun provideLessonMapper(dateFormatter: DateFormatter): LessonMapper = LessonMapper(dateFormatter)

    @App
    @Provides
    fun provideDayMapper(
        lessonMapper: LessonMapper,
        dateFormatter: DateFormatter
    ): DayMapper = DayMapper(lessonMapper, dateFormatter)
}