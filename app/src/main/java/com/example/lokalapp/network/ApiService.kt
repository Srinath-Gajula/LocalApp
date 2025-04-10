package com.example.lokalapp.network

import com.example.lokalapp.model.Job
import retrofit2.http.GET
import retrofit2.http.Query

//expected response structure
data class JobResponse(
    val results: List<Job>?
)

//Retrofit API Interface
interface ApiService {
    @GET("common/jobs")
    suspend fun getJobs(@Query("page") page: Int): JobResponse
}
