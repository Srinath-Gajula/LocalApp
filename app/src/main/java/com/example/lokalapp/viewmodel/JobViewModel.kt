package com.example.lokalapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lokalapp.model.Job
import com.example.lokalapp.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JobViewModel(private val repository: JobRepository) : ViewModel() {

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    private val _bookmarkedJobs = MutableStateFlow<List<Job>>(emptyList())
    val bookmarkedJobs: StateFlow<List<Job>> = _bookmarkedJobs

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loadedPages = mutableSetOf<Int>()

    fun fetchJobs(page: Int) {

        if (_loadedPages.contains(page)) return //Avoid reloading same page

        viewModelScope.launch {
            _loading.value = true
            try {
                val newJobs = repository.getJobs(page)
                _jobs.value = (_jobs.value + newJobs).distinctBy { it.id } //Avoid duplicates
                _error.value = null
                _loadedPages.add(page) //Mark this page as loaded
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun getJobById(jobId: String): Job? {
        val jobIdInt = jobId.toIntOrNull() ?: return null
        return _jobs.value.find { it.id == jobIdInt }
    }

//    fun bookmarkJob(job: Job) {
//        viewModelScope.launch {
//            try {
//                repository.insertJob(job)
//                val updatedList = _bookmarkedJobs.value.toMutableList().apply { add(job) }
//                _bookmarkedJobs.value = updatedList
//                Log.d("JobViewModel", "Bookmarked job: ${job.title}")
//            } catch (e: Exception) {
//                Log.e("JobViewModel", "Error bookmarking job: ${e.message}")
//            }
//        }
//    }


    fun bookmarkJob(job: Job) {
        // Avoid duplicates
        if (!_bookmarkedJobs.value.contains(job)) {
            _bookmarkedJobs.value = _bookmarkedJobs.value + job
        }
    }

    fun getBookmarks() {
        viewModelScope.launch {
            repository.getBookmarkedJobs().collect { bookmarks ->
                _bookmarkedJobs.value = bookmarks
            }
        }
    }

    fun unbookmarkJob(job: Job) {
        viewModelScope.launch {
            try {
                repository.deleteJob(job)
                _bookmarkedJobs.value = _bookmarkedJobs.value.filterNot { it.id == job.id }
            } catch (e: Exception) {
                Log.e("JobViewModel", "Failed to unbookmark job: ${e.message}")
            }
        }
    }

    fun isBookmarked(job: Job): Boolean {
        return bookmarkedJobs.value.any { it.id == job.id }
    }

    init {
        fetchJobs(1)
        getBookmarks()
    }


}
