package com.example.lokalapp.repository

import android.util.Log
import com.example.lokalapp.database.JobDao
import com.example.lokalapp.model.Job
import com.example.lokalapp.network.RetrofitInstance.api

class JobRepository(private val jobDao: JobDao) {

    //Fetching jobs from API (matching correct response structure)
    suspend fun getJobs(page: Int): List<Job> {
        return try {
            val response = api.getJobs(page)
            response.results ?: emptyList()
        } catch (e: Exception) {
            Log.e("API_ERROR", "Failed to fetch jobs: ${e.localizedMessage}")
            emptyList()
        }
    }

    suspend fun insertJob(job: Job) = jobDao.insertJob(job)

    suspend fun deleteJob(job: Job) = jobDao.deleteJob(job)

    fun getBookmarkedJobs() = jobDao.getBookmarkedJobs()
}
