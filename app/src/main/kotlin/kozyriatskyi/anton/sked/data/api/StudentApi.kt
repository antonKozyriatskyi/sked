package kozyriatskyi.anton.sked.data.api

import kozyriatskyi.anton.sked.data.pojo.LoginItem
import kozyriatskyi.anton.sked.data.pojo.UpdateScheduleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StudentApi {

    @GET("student/faculties")
    fun getFaculties(): Call<List<LoginItem>>

    @GET("student/courses")
    fun getCourses(@Query("faculty") facultyId: String): Call<List<LoginItem>>

    @GET("student/groups")
    fun getGroups(@Query("faculty") facultyId: String, @Query("course") courseId: String): Call<List<LoginItem>>

    @GET("student/schedule")
    fun getSchedule(
        @Query("faculty") facultyId: String,
        @Query("course") courseId: String,
        @Query("group") groupId: String,
        @Query("dateFrom") dateStart: String,
        @Query("dateTo") dateEnd: String
    ): Call<UpdateScheduleResponse>
}

