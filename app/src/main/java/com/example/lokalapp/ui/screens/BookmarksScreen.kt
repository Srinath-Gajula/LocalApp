package com.example.lokalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lokalapp.viewmodel.JobViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.lokalapp.ui.navigation.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(viewModel: JobViewModel, navController: NavController) {
    val bookmarkedJobs by viewModel.bookmarkedJobs.collectAsState()

    val systemUiController = rememberSystemUiController()

    // Set status bar color and icons
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color(0xFF1E88E5), // Any color you like
            darkIcons = false // true for dark icons on light bg
        )
    }

    Scaffold(
//        topBar = { TopAppBar(title = { Text("Bookmarks") }) }
        topBar = {
            TopAppBar(
                title = {
                    Text("Bookmarks", color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5), // Deep blue top bar
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (bookmarkedJobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,

            ) {
                Text("No bookmarks yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bookmarkedJobs) { job ->
                    JobCard(
                        job = job,
                        onClick = {
                            navController.navigate(Screen.JobDetails.route + "/${job.id}")
                        },
                        onBookmark = {
                            if (viewModel.isBookmarked(job)) {
                                viewModel.unbookmarkJob(job)
                            } else {
                                viewModel.bookmarkJob(job)
                            }
                        }
                    )
                }
            }
        }
    }
}
