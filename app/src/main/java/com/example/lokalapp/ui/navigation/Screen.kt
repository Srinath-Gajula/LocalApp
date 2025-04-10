package com.example.lokalapp.ui.navigation

import com.example.lokalapp.R

sealed class Screen(val route: String, val title: String, val icon: Int) {
    object Jobs : Screen("jobs", "Jobs", R.drawable.ic_jobs)
    object Bookmarks : Screen("bookmarks", "Bookmarks", R.drawable.ic_bookmark)
    object JobDetails : Screen("jobDetails", "Job Details", R.drawable.ic_details)
}