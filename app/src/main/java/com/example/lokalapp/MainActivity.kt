package com.example.lokalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.lokalapp.database.JobDatabase
import com.example.lokalapp.repository.JobRepository
import com.example.lokalapp.ui.components.BottomNavigationBar
import com.example.lokalapp.ui.navigation.NavGraph
import com.example.lokalapp.viewmodel.JobViewModel
import com.example.lokalapp.viewmodel.JobViewModelFactory

class MainActivity : ComponentActivity() {
    private val database by lazy { JobDatabase.getDatabase(this) } // Database instance
    private val repository by lazy { JobRepository(database.jobDao()) } // Pass JobDao to Repository
    private val viewModel: JobViewModel by viewModels { JobViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ) { padding ->
                NavGraph(navController = navController, viewModel = viewModel, padding = padding)
            }
        }
    }
}
