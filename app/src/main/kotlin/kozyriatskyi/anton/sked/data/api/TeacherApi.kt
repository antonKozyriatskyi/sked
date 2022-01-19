package kozyriatskyi.anton.sked.data.api

import kozyriatskyi.anton.sked.data.pojo.LoginItem
import kozyriatskyi.anton.sked.data.pojo.UpdateScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TeacherApi {

    @GET("teacher/departments")
    suspend fun getDepartments(): List<LoginItem>

    @GET("teacher/teachers")
    suspend fun getTeachers(@Query("department") departmentId: String): List<LoginItem>

    @GET("teacher/schedule")
    suspend fun getSchedule(
        @Query("department") departmentId: String,
        @Query("teacher") teacherId: String,
        @Query("dateFrom") dateStart: String,
        @Query("dateTo") dateEnd: String
    ): UpdateScheduleResponse
}

