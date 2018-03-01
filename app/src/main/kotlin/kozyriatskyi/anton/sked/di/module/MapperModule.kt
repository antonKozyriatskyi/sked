package kozyriatskyi.anton.sked.di.module

import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.LessonMapper
import kozyriatskyi.anton.sked.data.repository.DateFormatter
import kozyriatskyi.anton.sked.data.repository.ResourceManager

@Module
class MapperModule {

    @Provides
    fun provideLessonMapper(resourceManager: ResourceManager): LessonMapper = LessonMapper(resourceManager)

    @Provides
    fun provideDayMapper(lessonMapper: LessonMapper, dateFormatter: DateFormatter): DayMapper =
            DayMapper(lessonMapper, dateFormatter)
}