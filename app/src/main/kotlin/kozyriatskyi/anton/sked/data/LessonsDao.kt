package kozyriatskyi.anton.sked.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kozyriatskyi.anton.sked.data.pojo.LessonDb


/**
 * Created by Anton on 27.07.2017.
 */

@Dao
interface LessonsDao {

    // dd-MM-yyyy
    @Query("SELECT * FROM lessons WHERE date = :date ORDER BY number")
    fun observeAllByDate(date: String): Flow<List<LessonDb>>

    @Insert
    fun insertAll(lessons: List<LessonDb>)

    @Query("DELETE FROM lessons")
    fun deleteAll()

    @Query("SELECT count(*) FROM lessons WHERE date = :date")
    fun amountOfLessonsByDate(date: String): Flow<Int>
}