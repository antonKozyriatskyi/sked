package kozyriatskyi.anton.sked.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.data.pojo.LessonDb
import java.time.LocalDate


/**
 * Created by Anton on 27.07.2017.
 */

@Dao
interface LessonsDao {

    // dd-MM-yyyy
    @Query("SELECT * FROM lessons WHERE date(date) = date(:date) ORDER BY number")
    fun observeAllByDate(date: LocalDate): Flow<List<LessonDb>>

    // dd-MM-yyyy
    @Query("SELECT * FROM lessons WHERE date(date) BETWEEN date(:start) AND date(:end) ORDER BY date(date) ASC, number ASC")
    fun observeAllBetweenDates(start: LocalDate, end: LocalDate): Flow<List<LessonDb>>

    @Insert
    fun insertAll(lessons: List<LessonDb>)

    @Query("DELETE FROM lessons")
    fun deleteAll()

    @Query("SELECT count(*) FROM lessons WHERE date(date) = date(:date)")
    fun amountOfLessonsByDate(date: LocalDate): Flow<Int>
}