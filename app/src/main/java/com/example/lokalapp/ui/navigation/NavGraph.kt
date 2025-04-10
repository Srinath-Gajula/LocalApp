package com.example.lokalapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lokalapp.ui.screens.BookmarksScreen
import com.example.lokalapp.ui.screens.JobDetailsScreen
import com.example.lokalapp.ui.screens.JobsScreen
import com.example.lokalapp.viewmodel.JobViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: JobViewModel, padding: PaddingValues) {
    NavHost(navController = navController, startDestination = Screen.Jobs.route) {

        composable(Screen.Jobs.route) {
            JobsScreen(navController = navController, viewModel = viewModel, padding = padding)
        }

        composable(Screen.Bookmarks.route) {
            BookmarksScreen(viewModel = viewModel, navController = navController)
        }

        composable(Screen.JobDetails.route + "/{jobId}") { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            JobDetailsScreen(navController, jobId, viewModel)
        }

        composable(Screen.Bookmarks.route) {
            BookmarksScreen(viewModel = viewModel, navController = navController)
        }
    }
}
