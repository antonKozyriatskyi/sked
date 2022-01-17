package kozyriatskyi.anton.sked.data.api

import kozyriatskyi.anton.sked.data.pojo.LoginItem
import kozyriatskyi.anton.sked.data.pojo.UpdateScheduleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TeacherApi {

    @GET("teacher/departments")
    fun getDepartments(): Call<List<LoginItem>>

    @GET("teacher/teachers")
    fun getTeachers(@Query("department") departmentId: String): Call<List<LoginItem>>

    @GET("teacher/schedule")
    fun getSchedule(
        @Query("department") departmentId: String,
        @Query("teacher") teacherId: String,
        @Query("dateFrom") dateStart: String,
        @Query("dateTo") dateEnd: String
    ): Call<UpdateScheduleResponse>
}

