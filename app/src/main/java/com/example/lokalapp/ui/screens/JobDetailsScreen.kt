package com.example.lokalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lokalapp.model.Job
import com.example.lokalapp.viewmodel.JobViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(navController: NavController, jobId: String, viewModel: JobViewModel) {
    val job by produceState<Job?>(null, jobId) {
        value = viewModel.getJobById(jobId)
    }

    val bookmarkedJobs by viewModel.bookmarkedJobs.collectAsState()
    val isBookmarked = bookmarkedJobs.any { it.id == job?.id }

    val systemUiController = rememberSystemUiController()

    // Set status bar color and icons
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color(0xFF1E88E5), // Any color you like
            darkIcons = false // true for dark icons on light bg
        )
    }

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Job Details") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Job Details",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5), // Beautiful deep blue
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (job != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Title: ${job?.title ?: "No Title Available"}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("📍 Location: ${job?.location ?: "Unknown"}")
                Text("💰 Salary: ${job?.salary ?: "Not Provided"}")
                Text("📞 Phone: ${job?.phone ?: "Not Available"}")
                Text("📝 Description: ${job?.description ?: "No details"}")

                Spacer(modifier = Modifier.height(16.dp))

                job?.let { nonNullJob ->
                    Button(
                        onClick = { viewModel.bookmarkJob(nonNullJob) },
                        enabled = !isBookmarked
                    ) {
                        Text(if (isBookmarked) "Bookmarked" else "Bookmark Job")
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Job not found",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

