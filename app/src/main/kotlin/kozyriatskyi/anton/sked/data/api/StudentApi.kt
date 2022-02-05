package kozyriatskyi.anton.sked.data.api

import kozyriatskyi.anton.sked.data.pojo.LoginItem
import kozyriatskyi.anton.sked.data.pojo.UpdateScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StudentApi {

    @GET("student/faculties")
    suspend fun getFaculties(): List<LoginItem>

    @GET("student/courses")
    suspend fun getCourses(@Query("faculty") facultyId: String): List<LoginItem>

    @GET("student/groups")
    suspend fun getGroups(@Query("faculty") facultyId: String, @Query("course") courseId: String): List<LoginItem>

    @GET("student/schedule")
    suspend fun getSchedule(
        @Query("faculty") facultyId: String,
        @Query("course") courseId: String,
        @Query("group") groupId: String,
        @Query("dateFrom") dateStart: String,
        @Query("dateTo") dateEnd: String
    ): UpdateScheduleResponse
}

