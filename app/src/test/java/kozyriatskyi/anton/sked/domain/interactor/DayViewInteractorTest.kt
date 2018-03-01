package kozyriatskyi.anton.sked.domain.interactor

import io.reactivex.Flowable
import kozyriatskyi.anton.sked.data.pojo.Day
import kozyriatskyi.anton.sked.data.pojo.DayMapper
import kozyriatskyi.anton.sked.data.pojo.DayUi
import kozyriatskyi.anton.sked.domain.repository.ScheduleStorage
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * Created by Anton on 10.09.2017.
 */
class DayViewInteractorTest {

    lateinit var interactor: DayViewInteractor

    //    @Mock
    lateinit var scheduleRepository: ScheduleStorage
    //    @Mock
    lateinit var dayMapper: DayMapper

    @Before
    fun before() {
//        MockitoAnnotations.initMocks(this)
        scheduleRepository = Mockito.mock(ScheduleStorage::class.java)
        dayMapper = Mockito.mock(DayMapper::class.java)

    }

    @Test
    fun lessons_AllSuccess() {
        val dayUi = DayUi(1, 1, "11.09.2017", "11.09", emptyList())
        val day = Day(1, 1, "11.09.2017", emptyList())
        Mockito.`when`(dayMapper.dbToUi(day)).thenReturn(dayUi)
        Mockito.`when`(scheduleRepository.getLessonsByDate(1, 1, "11.09.2017")).thenReturn(Flowable.just(day))
        interactor = DayViewInteractor(scheduleRepository)
        interactor.lessons(1, 1)


        Assert.assertEquals(interactor.lessons(1, 1), dayUi)
    }
}