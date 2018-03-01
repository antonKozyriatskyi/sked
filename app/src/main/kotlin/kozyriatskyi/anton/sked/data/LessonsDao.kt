package kozyriatskyi.anton.sked.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import kozyriatskyi.anton.sked.data.pojo.LessonDb


/**
 * Created by Anton on 27.07.2017.
 */

@Dao
interface LessonsDao {

    @Query("SELECT * FROM lessons")
    fun getAll(): List<LessonDb>

    @Query("SELECT * FROM lessons WHERE date = :date ORDER BY number")
    // dd-MM-yyyy
    fun getAllByDate(date: String): Flowable<List<LessonDb>>

    @Query("SELECT * FROM lessons WHERE date = :date ORDER BY number")
    // dd-MM-yyyy
    fun getAllByDateList(date: String): List<LessonDb>

    @Insert
    fun insertAll(lessons: List<LessonDb>): List<Long>

    @Query("DELETE FROM lessons")
    fun deleteAll()

    @Query("DELETE FROM lessons WHERE date = :date")
    fun deleteAllByDate(date: String)

    @Query("SELECT count(*) FROM lessons WHERE date = :date")
    fun amountOfLessonsByDate(date: String): Flowable<Int>
}