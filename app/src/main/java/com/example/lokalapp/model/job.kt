package com.example.lokalapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_jobs")
data class Job(
    @PrimaryKey val id: Int,
    val title: String,
    val location: String?,
    val salary: String?,
    val phone: String?,
    val description: String?
)



