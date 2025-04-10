package com.example.lokalapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lokalapp.model.Job
import com.example.lokalapp.ui.navigation.Screen
import com.example.lokalapp.viewmodel.JobViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private const val MAX_PAGE = 15

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsScreen(navController: NavController, viewModel: JobViewModel, padding: PaddingValues) {
    val jobs by viewModel.jobs.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    var page by remember { mutableStateOf(1) }

    val listState = rememberLazyListState()

    val bookmarks by viewModel.bookmarkedJobs.collectAsState()

    val systemUiController = rememberSystemUiController()

    // Set status bar color and icons
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color(0xFF1E88E5), // Any color you like
            darkIcons = false // true for dark icons on light bg
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem?.index to totalItems
        }.collect { (lastVisible, total) ->
            val threshold = 3
            if (lastVisible != null && lastVisible >= total - threshold && !isLoading && page < MAX_PAGE) {
                page++
                viewModel.fetchJobs(page)
            }
        }
    }

    Scaffold(
        //topBar = { TopAppBar(title = { Text("Jobs") }) }
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Jobs",
                        color = Color.White // bright text against dark background
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5), // Blue shade (like Indigo 600)
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading && jobs.isEmpty() -> {
                    CircularProgressIndicator(color = Color(0xFF1E88E5),modifier = Modifier.align(Alignment.Center))
                }
                errorMessage != null -> {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                jobs.isEmpty() -> {
                    Text(
                        text = "No jobs available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(jobs) { index, job ->
                            val bookmarked = bookmarks.any { it.id == job.id }

                            JobCard(
                                job = job,
                                isBookmarked = bookmarked,
                                onBookmark = { viewModel.bookmarkJob(job) },
                                onClick = { navController.navigate(Screen.JobDetails.route + "/${job.id}") }
                            )

                            if (index == jobs.lastIndex && !isLoading && errorMessage == null) {
                                if (page < MAX_PAGE) {
                                    page++
                                    viewModel.fetchJobs(page)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JobCard(
    job: Job,
    isBookmarked: Boolean = true,
    onBookmark: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = job.title ?: "Untitled Job",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("📍 Location: ${job.location ?: "Not Available"}")
            Text("💰 Salary: ${job.salary ?: "Not Provided"}")
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onBookmark,
                enabled = !isBookmarked
            ) {
                Text(if (isBookmarked) "Bookmarked" else "Bookmark")
            }
        }
    }
}