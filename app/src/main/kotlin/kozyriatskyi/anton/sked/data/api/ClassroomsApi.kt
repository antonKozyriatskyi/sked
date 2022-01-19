package kozyriatskyi.anton.sked.data.api

import kozyriatskyi.anton.sked.data.pojo.AudienceNetwork
import kozyriatskyi.anton.sked.data.pojo.GetClassroomsTimesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ClassroomsApi {

    @GET("classrooms/times")
    suspend fun getTimes(): GetClassroomsTimesResponse

    @GET("classrooms")
    suspend fun getAudiences(
        @Query("date") date: String,
        @Query("lessonStart") lessonStart: String,
        @Query("lessonEnd") lessonEnd: String
    ): List<AudienceNetwork>
}