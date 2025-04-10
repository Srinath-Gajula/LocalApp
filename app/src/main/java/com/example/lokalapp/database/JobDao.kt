package com.example.lokalapp.database

import androidx.room.*
import com.example.lokalapp.model.Job
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: Job)

    @Delete
    suspend fun deleteJob(job: Job)

    @Query("SELECT * FROM bookmarked_jobs")
    fun getBookmarkedJobs(): Flow<List<Job>>
}
